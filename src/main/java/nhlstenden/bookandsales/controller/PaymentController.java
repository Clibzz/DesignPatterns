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

    private boolean isLoggedIn(HttpSession session)
    {
        return session.getAttribute("isLoggedIn") != null && (boolean) session.getAttribute("isLoggedIn");
    }

    private JSONArray getAllCarts() throws IOException, JSONException
    {
        Path path = Paths.get("carts.json");
        String currentContent = Files.readString(path);

        return new JSONArray(currentContent);
    }

    private void fillCartJsonOfUser(HttpSession session) throws IOException, JSONException
    {
        Path path = Paths.get(session.getAttribute("username") + ".json");
        JSONArray jsonData = this.getAllCarts();
        JSONArray userSpecificJsonData = new JSONArray();

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

    private void removeUserItemsFromAllCartsJson(HttpSession session) throws JSONException, IOException
    {
        Path path = Paths.get("carts.json");
        JSONArray jsonData = this.getAllCarts();
        for (int i = 0; i < jsonData.length(); i++)
        {
            JSONObject jsonObj = jsonData.getJSONObject(i);
            if (jsonObj.getInt("userId") == (int) session.getAttribute("userId"))
            {
                jsonData.remove(i);
            }
        }

        File file = new File(path.toString());
        if (file.delete())
        {
            try (FileWriter writer = new FileWriter(path.toFile(), false))
            {
                writer.write(jsonData.toString(4));
            }
        }
    }

    private JSONArray getUserCart(HttpSession session) throws IOException, JSONException
    {
        Path path = Paths.get(session.getAttribute("username") + ".json");
        String userContent = Files.readString(path);

        return new JSONArray(userContent);
    }

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

    private boolean isNotEmptyCart(HttpSession session) throws IOException, JSONException
    {
        Path path = Paths.get(session.getAttribute("username") + ".json");
        JSONArray jsonArray = new JSONArray(Files.readString(path));

        return jsonArray.length() > 0;
    }

    private void setIsNotEmptyCartVariable(Model model, HttpSession session) throws JSONException, IOException
    {
        model.addAttribute("isNotEmptyCart", this.isNotEmptyCart(session));
    }

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

    public double getTotalPrice(HttpSession session) throws JSONException, IOException
    {
        double totalPayAmount = 0;

        for (BookProduct bookProduct : this.getBooksInCart(session))
        {
            totalPayAmount += (bookProduct.getPrice());
        }

        return totalPayAmount;
    }

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
            redirectAttributes.addFlashAttribute("errorMessage", "The login credentials are invalid, please try again!");
            return;
        }

        // Check if payment is successful
        if (this.paymentService.checkout(this.getTotalPrice(session)))
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

    @GetMapping("/payment-complete")
    public String paymentComplete(Model model)
    {
        return "cart";
    }
}