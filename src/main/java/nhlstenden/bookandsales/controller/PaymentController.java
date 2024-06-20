package nhlstenden.bookandsales.controller;

import jakarta.servlet.http.HttpSession;
import nhlstenden.bookandsales.factory.BookFactory;
import nhlstenden.bookandsales.factory.BookProduct;
import nhlstenden.bookandsales.model.BookType;
import nhlstenden.bookandsales.model.Genre;
import nhlstenden.bookandsales.model.PaymentCartHistory;
import nhlstenden.bookandsales.model.PaymentCartMemento;
import nhlstenden.bookandsales.service.PaymentService;
import nhlstenden.bookandsales.strategy.GiftCardStrategy;
import nhlstenden.bookandsales.strategy.INGStrategy;
import nhlstenden.bookandsales.strategy.PaymentStrategy;
import nhlstenden.bookandsales.strategy.PaypalStrategy;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

@Controller
public class PaymentController
{
    private final PaymentService paymentService;
    private final BookFactory bookFactory;

    public PaymentController(PaymentService paymentService)
    {
        this.paymentService = paymentService;
        this.bookFactory = new BookFactory();
    }

    /**
     * Check if a user is logged in
     * @param session The current session
     * @return True if logged in, false if not logged in
     */
    private boolean isLoggedIn(HttpSession session)
    {
        return session.getAttribute("isLoggedIn") != null && (boolean) session.getAttribute("isLoggedIn");
    }

    /**
     * Get all carts information of all users
     * @return A JSONArray with all he carts information
     * @throws IOException Throws an IOException when something goes wrong retrieving the data from the file
     * @throws JSONException Throws a JSONException when something goes wrong reading the file
     */
    private JSONArray getAllCarts() throws IOException, JSONException
    {
        Path path = Paths.get("carts.json");
        String currentContent = Files.readString(path);

        return new JSONArray(currentContent);
    }

    /**
     * Fill the user's personal cart file
     * @param session The current session
     * @throws IOException Throws an IOException when something goes wrong writing the data to the file
     * @throws JSONException Throws a JSONException when something goes wrong related to the JSON
     */
    private void fillCartJsonOfUser(HttpSession session) throws IOException, JSONException
    {
        //Retrieving the chosen user
        Path path = Paths.get(session.getAttribute("username") + ".json");
        JSONArray jsonData = this.getAllCarts();
        JSONArray userSpecificJsonData = new JSONArray();

        //looping through the data and comparing to see if the user exists inside the carts.json
        for (int i = 0; i < jsonData.length(); i++)
        {
            JSONObject jsonObj = jsonData.getJSONObject(i);
            if (jsonObj.getInt("userId") == (int) session.getAttribute("userId"))
            {
                userSpecificJsonData.put(jsonObj);
            }
        }
        try (FileWriter writer = new FileWriter(path.toFile(), false))
        {
            writer.write(userSpecificJsonData.toString(4));
        }

        PaymentCartHistory history = new PaymentCartHistory();
        history.saveState(new PaymentCartMemento(userSpecificJsonData.toString()));
    }

    /**
     * Remove a user's cart from the general carts file when the user checks out their cart
     * @param session The current session
     * @throws JSONException Throws a JSONException when something goes wrong related to the JSON
     * @throws IOException Throws an IOException when something goes wrong removing the data from the file
     */
    private void removeUserItemsFromAllCartsJson(HttpSession session) throws JSONException, IOException {
        //Getting the cart.json file path
        Path path = Paths.get("carts.json");
        JSONArray jsonData = this.getAllCarts();
        int userId = (int) session.getAttribute("userId");

        //Loop through all the created carts in reverse order
        for (int i = jsonData.length() - 1; i >= 0; i--) {
            JSONObject jsonObj = jsonData.getJSONObject(i);
            //Check if the user is present in the cart.json
            if (jsonObj.getInt("userId") == userId) {
                //Remove if found
                jsonData.remove(i);
            }
        }

        // Write the updated JSON data back to the file
        try (FileWriter writer = new FileWriter(path.toFile(), false)) {
            writer.write(jsonData.toString(4));
        }
    }


    /**
     * Get the data from the user's personal cart file
     * @param session The current session
     * @return The JSONArray with cart data
     * @throws IOException Throws an IOException when something goes wrong retrieving the data from the file
     * @throws JSONException Throws a JSONException when something goes wrong related to the JSON
     */
    private JSONArray getUserCart(HttpSession session) throws IOException, JSONException
    {
        Path path = Paths.get(session.getAttribute("username") + ".json");
        String userContent = Files.readString(path);

        return new JSONArray(userContent);
    }

    /**
     * Get a list of the books in a user's cart
     * @param session The current session
     * @return A list of the books in a user's cart
     * @throws JSONException Throws a JSONException when something goes wrong related to the JSON
     * @throws IOException Throws an IOException when something goes wrong retrieving the data from the file
     */
    private ArrayList<BookProduct> getBooksInCart(HttpSession session) throws JSONException, IOException
    {
        ArrayList<BookProduct> booksInCart = new ArrayList<>();

        JSONArray jsonData = this.getUserCart(session);

        for (int i = 0; i < jsonData.length(); i++)
        {
            JSONObject jsonObject = jsonData.getJSONObject(i);
            if (jsonObject.getInt("userId") == (int) session.getAttribute("userId"))
            {
                booksInCart.add(this.bookFactory.createBookProduct(
                        jsonObject.getInt("id"),
                        new BookType(jsonObject.getJSONObject("bookType").getInt("id"),
                                jsonObject.getJSONObject("bookType").getString("type"),
                                jsonObject.getJSONObject("bookType").getString("attributeType"),
                                jsonObject.getJSONObject("bookType").getBoolean("hasAttribute")),
                        jsonObject.getString("title"),
                        jsonObject.getDouble("price"),
                        jsonObject.getString("author"),
                        jsonObject.getString("publisher"),
                        jsonObject.getInt("pageAmount"),
                        Genre.valueOf(jsonObject.getString("genre")),
                        jsonObject.getJSONObject("bookType").getBoolean("hasAttribute"),
                        jsonObject.getString("description"),
                        jsonObject.getString("image")
                ));
            }
        }

        return booksInCart;
    }

    /**
     * Check if the cart of a user is empty
     * @param session The current session
     * @return True if filled, false if empty
     * @throws IOException Throws an IOException when something goes wrong reading the file
     * @throws JSONException Throws a JSONException when something goes wrong related to the JSON file
     */
    private boolean isNotEmptyCart(HttpSession session) throws IOException, JSONException
    {
        Path path = Paths.get(session.getAttribute("username") + ".json");
        JSONArray jsonArray = new JSONArray(Files.readString(path));

        return jsonArray.length() > 0;
    }

    /**
     * Set a variable in the page's model
     * @param model The model of the page
     * @param session The current session
     * @throws JSONException Throws a JSONException when something goes wrong related to the JSON file
     * @throws IOException Throws an IOException when something goes wrong reading the file
     */
    private void setIsNotEmptyCartVariable(Model model, HttpSession session) throws JSONException, IOException
    {
        model.addAttribute("isNotEmptyCart", this.isNotEmptyCart(session));
    }

    /**
     * Retrieving the cart page
     * @param session The current session
     * @param model The model of the page
     * @return The cart page
     * @throws JSONException Throws a JSONException when something goes wrong related to the JSON file
     * @throws IOException Throws an IOException when something goes wrong reading the file
     */
    @GetMapping("/cart")
    public String getCartOfUser(HttpSession session, Model model) throws JSONException, IOException
    {
        if (this.isLoggedIn(session))
        {
            this.fillCartJsonOfUser(session);
            model.addAttribute("booksFromUser", this.getBooksInCart(session));
            this.setIsNotEmptyCartVariable(model, session);
            this.fillCartJsonOfUser(session);

            return "cart";
        }

        return "redirect:/login";
    }

    /**
     * Choose a payment strategy
     * @param paymentType The type of payment
     * @param session The current session
     * @param model The model of the page
     * @return The cart page
     * @throws JSONException Throws a JSONException when something goes wrong related to the JSON file
     * @throws IOException Throws an IOException when something goes wrong reading the file
     */
    @PostMapping("/cart")
    public String choosePaymentStrategy(@RequestParam("paymentType") String paymentType,
                                        HttpSession session, Model model) throws JSONException, IOException
    {
        if (this.isLoggedIn(session))
        {
            this.fillCartJsonOfUser(session);
            model.addAttribute("paymentStrategy", paymentType);
            model.addAttribute("booksFromUser", this.getBooksInCart(session));
            this.setIsNotEmptyCartVariable(model, session);

            return "cart";
        }

        return "redirect:/login";
    }

    /**
     * Get the total price of the cart
     * @param session The current session
     * @return The total price of the cart
     * @throws JSONException Throws a JSONException when something goes wrong related to the JSON file
     * @throws IOException Throws an IOException when something goes wrong reading the file
     */
    public double getTotalPrice(HttpSession session) throws JSONException, IOException
    {
        double totalPayAmount = 0;

        for (BookProduct bookProduct : this.getBooksInCart(session))
        {
            totalPayAmount += (bookProduct.getPrice());
        }

        return totalPayAmount;
    }

    /**
     * Handle payments
     * @param paymentStrategy The chosen payment strategy
     * @param redirectAttributes The redirectAttributes to show the user success & error messages
     * @param session The current session
     * @throws JSONException Throws a JSONException when something goes wrong related to the JSON file
     * @throws IOException Throws an IOException when something goes wrong mutating the file
     */
    private void handlePayment(PaymentStrategy paymentStrategy, RedirectAttributes redirectAttributes,
                               HttpSession session) throws JSONException, IOException
    {
        if (paymentStrategy == null)
        {
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid payment strategy selected!");
            return;
        }

        // Check if login credentials are invalid
        if (paymentStrategy.getMoneyAmount() == -1)
        {
            redirectAttributes.addFlashAttribute("errorMessage", "The (login) credentials are invalid, please try again!");
            return;
        }

        // Check if payment is successful
        if (this.paymentService.hasCheckedOut(this.getTotalPrice(session)))
        {
            redirectAttributes.addFlashAttribute("successMessage", "Payment successful!");
            this.removeUserItemsFromAllCartsJson(session);
        } else
        {
            if (paymentStrategy instanceof GiftCardStrategy)
            {
                redirectAttributes.addFlashAttribute("errorMessage", "The gift card does not have the required balance to pay for these products, please try again!");
            } else
            {
                redirectAttributes.addFlashAttribute("errorMessage", "The account does not have the required balance to pay for these products, please try again!");
            }
        }
    }

    /**
     * Post for paying with ING
     * @param username The username
     * @param password The password
     * @param bankNumber The bank number
     * @param session The current session
     * @param model The model of the page
     * @param redirectAttributes The redirectAttributes to show the user success & error messages
     * @return The cart page
     * @throws JSONException Throws a JSONException when something goes wrong related to the JSON file
     * @throws IOException Throws an IOException when something goes wrong mutating the file
     */
    @PostMapping("/ing-pay")
    public String ingPay(@RequestParam("username") String username, @RequestParam("password") String password,
                         @RequestParam("bankNumber") String bankNumber, HttpSession session, Model model,
                         RedirectAttributes redirectAttributes) throws JSONException, IOException
    {
        model.addAttribute("booksFromUser", this.getBooksInCart(session));

        this.paymentService.setPaymentStrategy(new INGStrategy(bankNumber, username, password));
        PaymentStrategy paymentStrategy = this.paymentService.getPaymentStrategy();

        handlePayment(paymentStrategy, redirectAttributes, session);

        return "redirect:/cart";
    }

    /**
     * Post for paying with paypal
     * @param paypalUser The username
     * @param paypalPassword The password
     * @param session The current session
     * @param model The model of the page
     * @param redirectAttributes The redirectAttributes to show the user success & error messages
     * @return The cart page
     * @throws JSONException Throws a JSONException when something goes wrong related to the JSON file
     * @throws IOException Throws an IOException when something goes wrong mutating the file
     */
    @PostMapping("/paypal-pay")
    public String paypalPay(@RequestParam("paypalUser") String paypalUser,
                            @RequestParam("paypalPassword") String paypalPassword, HttpSession session,
                            Model model, RedirectAttributes redirectAttributes) throws JSONException, IOException
    {
        model.addAttribute("booksFromUser", this.getBooksInCart(session));

        this.paymentService.setPaymentStrategy(new PaypalStrategy(paypalUser, paypalPassword));
        PaymentStrategy paymentStrategy = this.paymentService.getPaymentStrategy();
        handlePayment(paymentStrategy, redirectAttributes, session);

        return "redirect:/cart";
    }

    /**
     * Post for paying with a gift card
     * @param giftCard The code of the gift card
     * @param session The current session
     * @param model The model of the page
     * @param redirectAttributes The redirectAttributes to show the user success & error messages
     * @return The cart page
     * @throws JSONException Throws a JSONException when something goes wrong related to the JSON file
     * @throws IOException Throws an IOException when something goes wrong mutating the file
     */
    @PostMapping("/giftcard-pay")
    public String giftcardPay(@RequestParam("giftCard") String giftCard, HttpSession session, Model model,
                              RedirectAttributes redirectAttributes) throws JSONException, IOException
    {
        model.addAttribute("booksFromUser", this.getBooksInCart(session));
        this.paymentService.setPaymentStrategy(new GiftCardStrategy(giftCard));
        PaymentStrategy paymentStrategy = this.paymentService.getPaymentStrategy();
        handlePayment(paymentStrategy, redirectAttributes, session);

        return "redirect:/cart";
    }

    /**
     * Redirect to the payment-complete URL when paying is completed
     * @return The cart page
     */
    @GetMapping("/payment-complete")
    public String paymentComplete()
    {
        return "cart";
    }
}