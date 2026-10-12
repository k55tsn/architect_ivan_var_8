package util;

import model.User;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class UserLoader {
    public static List<User> load(String path) throws IOException {
        List<User> users = new ArrayList<>();
        for (String line : Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8)) {
            line = line.trim();
            if (line.isEmpty() || line.startsWith("#")) continue;
            String[] parts = line.split(";");
            if (parts.length >= 3) {
                users.add(new User(parts[0].trim(), parts[1].trim(), parts[2].trim()));
            }
        }
        return users;
    }
}