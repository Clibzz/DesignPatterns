package nhlstenden.bookandsales.Component;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class FolderInitializer implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args)
    {
        // Create a folder at Users/username/Documents
        String documentsDirectory = System.getProperty("user.home") + File.separator + "Documents";
        String targetDirectory = documentsDirectory + File.separator + "BookAndSales";

        File directory = new File(targetDirectory);
        if (!directory.exists()) {
            if (directory.mkdirs()) {
                System.out.println("Directory created successfully: " + targetDirectory);
            } else {
                System.err.println("Failed to create directory: " + targetDirectory);
            }
        }
    }
}

