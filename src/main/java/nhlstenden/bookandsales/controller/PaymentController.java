package nhlstenden.bookandsales.controller;

import jakarta.servlet.http.HttpSession;
import nhlstenden.bookandsales.service.PaymentService;
import nhlstenden.bookandsales.strategy.GiftCardStrategy;
import nhlstenden.bookandsales.strategy.INGStrategy;
import nhlstenden.bookandsales.strategy.PaypalStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PaymentController
{

        private PaymentService paymentService;

        public PaymentController(PaymentService paymentService)
        {
                this.paymentService = paymentService;
        }

        @GetMapping("/cart")
        public String getCart(HttpSession session, Model model)
        {
                if (isLoggedIn(session))
                {
                        model.addAttribute("message", "Cart");
                        return "cart";
                }
                return "redirect:/login";
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

        private boolean isLoggedIn(HttpSession session)
        {
                return session.getAttribute("isLoggedIn") != null && (boolean) session.getAttribute("isLoggedIn");
        }
}