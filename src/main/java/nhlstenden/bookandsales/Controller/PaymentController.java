package nhlstenden.bookandsales.Controller;

import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpSession;
import nhlstenden.bookandsales.Model.PaymentCartHistory;
import nhlstenden.bookandsales.Model.PaymentCartMemento;
import nhlstenden.bookandsales.service.PaymentService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
                this.createCartJsonOfUser(session);
                return "cart";
        }

        private JSONArray getAllCarts() throws IOException, JSONException
        {
                Path path = Paths.get("carts.json");
                String currentContent = Files.readString(path);
                return new JSONArray(currentContent);
        }
}
