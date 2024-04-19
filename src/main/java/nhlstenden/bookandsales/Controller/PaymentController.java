package nhlstenden.bookandsales.Controller;

import jakarta.servlet.http.HttpSession;
import nhlstenden.bookandsales.Model.PaymentCartHistory;
import nhlstenden.bookandsales.Model.PaymentCartMemento;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

@Controller
public class PaymentController
{
        public PaymentController()
        {

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
                        Path path = Paths.get("carts.json");
                        if (!Files.exists(path))
                        {
                                Files.createFile(path);
                                Files.write(path, Collections.singletonList("[]"));
                        }
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
}
