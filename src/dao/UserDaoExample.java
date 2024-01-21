package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.User;
import org.mindrot.jbcrypt.BCrypt;
import util.DatabaseConnection;

public class UserDaoExample {
    public UserDaoExample() {
    }

    public static boolean createUser(User var0) {
        boolean var1 = false;
        String var2 = BCrypt.hashpw(var0.getPassword(), BCrypt.gensalt());
        String var3 = "INSERT INTO users (first_name, last_name, email, password, is_doctor) VALUES (?, ?, ?, ?, ?)";

        try {
            Connection var4 = DatabaseConnection.getCon();
            PreparedStatement var5 = var4.prepareStatement(var3);
            var5.setString(1, var0.getFirstName());
            var5.setString(2, var0.getLastName());
            var5.setString(3, var0.getEmail());
            var5.setString(4, var2);
            var5.setBoolean(5, var0.isDoctor());
            int var6 = var5.executeUpdate();
            if (var6 != 0) {
                var1 = true;
            }
        } catch (SQLException var7) {
            var7.printStackTrace();
        }

        return var1;
    }

    public User getUserById(int var1) {
        int var2 = 0;
        String var3 = null;
        String var4 = null;
        String var5 = null;
        String var6 = null;
        boolean var7 = false;
        String var8 = "SELECT * FROM users WHERE id = ?";

        try {
            Connection var9 = DatabaseConnection.getCon();
            PreparedStatement var10 = var9.prepareStatement(var8);
            var10.setInt(1, var1);

            for(ResultSet var11 = var10.executeQuery(); var11.next(); var7 = var11.getBoolean("is_doctor")) {
                var2 = var11.getInt("id");
                var3 = var11.getString("first_name");
                var4 = var11.getString("last_name");
                var5 = var11.getString("email");
                var6 = var11.getString("password");
            }
        } catch (SQLException var12) {
            var12.printStackTrace();
        }

        return new User(var2, var3, var4, var5, var6, var7);
    }

    public static User getUserByEmail(String var0) {
        int var1 = 0;
        String var2 = null;
        String var3 = null;
        String var4 = null;
        String var5 = null;
        boolean var6 = false;
        String var7 = "SELECT * FROM users WHERE email = ?";

        try {
            Connection var8 = DatabaseConnection.getCon();
            PreparedStatement var9 = var8.prepareStatement(var7);
            var9.setString(1, var0);

            for(ResultSet var10 = var9.executeQuery(); var10.next(); var6 = var10.getBoolean("is_doctor")) {
                var1 = var10.getInt("id");
                var2 = var10.getString("first_name");
                var3 = var10.getString("last_name");
                var4 = var10.getString("email");
                var5 = var10.getString("password");
            }
        } catch (SQLException var11) {
            var11.printStackTrace();
        }

        return new User(var1, var2, var3, var4, var5, var6);
    }

    public boolean updateUser(User var1) {
        boolean var2 = false;
        String var3 = "UPDATE users SET first_name = ?, last_name = ?, email = ?, password = ?, is_doctor = ? WHERE id = ?";
        String var4 = BCrypt.hashpw(var1.getPassword(), BCrypt.gensalt());

        try {
            Connection var5 = DatabaseConnection.getCon();
            PreparedStatement var6 = var5.prepareStatement(var3);
            var6.setString(1, var1.getFirstName());
            var6.setString(2, var1.getLastName());
            var6.setString(3, var1.getEmail());
            var6.setString(4, var4);
            var6.setBoolean(5, var1.isDoctor());
            var6.setInt(6, var1.getId());
            int var7 = var6.executeUpdate();
            if (var7 != 0) {
                var2 = true;
            }
        } catch (SQLException var8) {
            var8.printStackTrace();
        }

        return var2;
    }

    public boolean deleteUser(int var1) {
        boolean var2 = false;
        String var3 = "DELETE FROM users WHERE id = ?";

        try {
            Connection var4 = DatabaseConnection.getCon();
            PreparedStatement var5 = var4.prepareStatement(var3);
            var5.setInt(1, var1);
            int var6 = var5.executeUpdate();
            if (var6 != 0) {
                var2 = true;
            }
        } catch (SQLException var7) {
            var7.printStackTrace();
        }

        return var2;
    }

    public static boolean verifyPassword(String var0, String var1) {
        boolean var2 = false;
        String var3 = "SELECT password FROM users WHERE email = ?";

        try {
            Connection var4 = DatabaseConnection.getCon();
            PreparedStatement var5 = var4.prepareStatement(var3);
            var5.setString(1, var0);
            ResultSet var6 = var5.executeQuery();

            String var7;
            for(var7 = null; var6.next(); var7 = var6.getString("password")) {
            }

            if (BCrypt.checkpw(var1, var7)) {
                var2 = true;
            }
        } catch (SQLException var8) {
            var8.printStackTrace();
        }

        return var2;
    }
}