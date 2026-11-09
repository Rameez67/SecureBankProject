package dao;

import config.DBConnection;
import dto.AccountDTO;
import security.EncryptionUtil;

import java.sql.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class AccountDAO {

    public int save(AccountDTO account) throws Exception {
        String encryptedAccount = EncryptionUtil.encrypt(account.getAccountNumber());
        String canonicalData = canonical(account);
        String hash = EncryptionUtil.sha256(canonicalData);

        String sql = """
                INSERT INTO accounts (holder_name, encrypted_account, balance, data_hash)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, account.getHolderName());
            statement.setString(2, encryptedAccount);
            statement.setBigDecimal(3, account.getBalance().setScale(2, RoundingMode.HALF_UP));
            statement.setString(4, hash);
            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        throw new SQLException("Account was saved but its ID could not be read.");
    }

    public AccountDTO findById(int id) throws Exception {
        String sql = """
                SELECT id, holder_name, encrypted_account, balance, data_hash
                FROM accounts WHERE id = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            try (ResultSet result = statement.executeQuery()) {
                if (!result.next()) return null;

                String holder = result.getString("holder_name");
                String encrypted = result.getString("encrypted_account");
                BigDecimal balance = result.getBigDecimal("balance");
                String storedHash = result.getString("data_hash");

                String accountNumber = EncryptionUtil.decrypt(encrypted);
                AccountDTO account = new AccountDTO(
                        result.getInt("id"), holder, accountNumber, balance);

                if (!EncryptionUtil.verifyHash(canonical(account), storedHash)) {
                    throw new SecurityException(
                            "Integrity check failed: stored data may have been modified.");
                }
                return account;
            }
        }
    }

    public boolean deleteById(int id) throws Exception {
        String sql = "DELETE FROM accounts WHERE id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        }
    }

    private String canonical(AccountDTO account) {
        // Length prefixes avoid ambiguous concatenations between fields.
        String holder = account.getHolderName();
        String number = account.getAccountNumber();
        String balance = account.getBalance().setScale(2, RoundingMode.HALF_UP).toPlainString();
        return holder.length() + ":" + holder
                + number.length() + ":" + number
                + balance.length() + ":" + balance;
    }
}
