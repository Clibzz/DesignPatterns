package nhlstenden.bookandsales.controller;

import jakarta.servlet.http.HttpSession;
import nhlstenden.bookandsales.model.PaymentCartHistory;
import nhlstenden.bookandsales.model.PaymentCartMemento;
import nhlstenden.bookandsales.service.PaymentService;
import nhlstenden.bookandsales.strategy.GiftCardStrategy;
import nhlstenden.bookandsales.strategy.INGStrategy;
import nhlstenden.bookandsales.strategy.PaypalStrategy;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

@Controller
public class PaymentController
{

        private PaymentService paymentService;

        public PaymentController(PaymentService paymentService)
        {
                this.paymentService = paymentService;
        }

        private boolean isLoggedIn(HttpSession session)
        {
                return session.getAttribute("isLoggedIn") != null && (boolean) session.getAttribute("isLoggedIn");
        }

        private void createCartJsonOfUser(HttpSession session) throws IOException, JSONException
        {
                Path path = Paths.get(session.getAttribute("username") + ".json");
                JSONArray jsonData = this.getAllCarts();
                JSONArray userSpecificJsonData = new JSONArray();

                for (int i = 0; i < jsonData.length(); i++)
                {
                        JSONObject jsonObj = jsonData.getJSONObject(i);
                        if (jsonObj.getInt("userId") == (int)session.getAttribute("userId"))
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

        @GetMapping("/cart")
        public String getCartOfUser(HttpSession session) throws JSONException, IOException
        {
                if (this.isLoggedIn(session))
                {
                        this.createCartJsonOfUser(session);
                        return "cart";
                }
                return "redirect:/login";
        }

        private JSONArray getAllCarts() throws IOException, JSONException
        {
                Path path = Paths.get("carts.json");
                String currentContent = Files.readString(path);
                return new JSONArray(currentContent);
        }

        @PostMapping("/cart")
        public String choosePaymentStrategy(@RequestParam("paymentType") String paymentType, Model model)
        {
                model.addAttribute("paymentStrategy", paymentType);
                return "cart";
        }

        @PostMapping("/ing-strategy")
        public String ING(@RequestParam("paymentType") String paymentType,
                          @RequestParam("username") String username,
                          @RequestParam("password") String password,
                          @RequestParam("bankNumber") String bankNumber,
                          Model model)
        {

                model.addAttribute("paymentStrategy", paymentType);
                this.paymentService.setPaymentStrategy(new INGStrategy(bankNumber, username, password));
                this.paymentService.checkout(0);

                return "cart";
        }

        @PostMapping("/paypal-strategy")
        public String paypal(@RequestParam("paymentType") String paymentType,
                             @RequestParam("paypalUser") String paypalUser,
                             @RequestParam("paypalPassword") String paypalPassword,
                             Model model)
        {

                model.addAttribute("paymentStrategy", paymentType);
                this.paymentService.setPaymentStrategy(new PaypalStrategy(paypalUser, paypalPassword));
                this.paymentService.checkout(0);

                return "cart";
        }

        @PostMapping("/giftcard-strategy")
        public String giftcard(@RequestParam("paymentType") String paymentType,
                               @RequestParam("giftCard") String giftCard, Model model)
        {

                model.addAttribute("paymentStrategy", paymentType);
                this.paymentService.setPaymentStrategy(new GiftCardStrategy(giftCard));
                this.paymentService.checkout(0);

                return "cart";
        }

        @GetMapping("/payment-complete")
        public String paymentComplete(Model model)
        {

                return "cart";
        }
}