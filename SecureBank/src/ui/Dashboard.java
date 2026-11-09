package ui;

import dao.AccountDAO;
import dto.AccountDTO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.math.BigDecimal;

public class Dashboard extends JFrame {
    private final JTextField holderField = new JTextField();
    private final JTextField accountField = new JTextField();
    private final JTextField balanceField = new JTextField();
    private final JTextField idField = new JTextField();
    private final JLabel statusLabel = new JLabel("Ready. Enter account details to begin.");
    private final AccountDAO accountDAO = new AccountDAO();

    private static final Color NAVY = new Color(18, 32, 55);
    private static final Color BLUE = new Color(35, 105, 180);
    private static final Color BG = new Color(242, 246, 251);
    private static final Color TEXT = new Color(40, 52, 70);

    public Dashboard() {
        setTitle("SecureBank | Account Security System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(850, 650);
        setMinimumSize(new Dimension(760, 600));
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG);
        setLayout(new BorderLayout());

        add(createHeader(), BorderLayout.NORTH);
        add(createMainPanel(), BorderLayout.CENTER);
        add(createFooter(), BorderLayout.SOUTH);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(NAVY);
        header.setBorder(new EmptyBorder(22, 30, 22, 30));

        JPanel titles = new JPanel();
        titles.setOpaque(false);
        titles.setLayout(new BoxLayout(titles, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("SECUREBANK");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 25));

        JLabel subtitle = new JLabel("ACCOUNT ENCRYPTION & SECURE STORAGE");
        subtitle.setForeground(new Color(180, 202, 226));
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 11));

        titles.add(title);
        titles.add(Box.createVerticalStrut(5));
        titles.add(subtitle);

        JLabel shield = new JLabel("  AES-256  ");
        shield.setOpaque(true);
        shield.setBackground(new Color(31, 76, 113));
        shield.setForeground(new Color(160, 225, 210));
        shield.setFont(new Font("Segoe UI", Font.BOLD, 13));
        shield.setBorder(new EmptyBorder(9, 10, 9, 10));

        header.add(titles, BorderLayout.WEST);
        header.add(shield, BorderLayout.EAST);
        return header;
    }

    private JPanel createMainPanel() {
        JPanel outer = new JPanel(new GridBagLayout());
        outer.setBackground(BG);
        outer.setBorder(new EmptyBorder(25, 30, 20, 30));

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(224, 231, 239)),
                new EmptyBorder(25, 28, 25, 28)));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 8, 8, 8);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.gridx = 0; c.gridy = 0; c.gridwidth = 2;

        JLabel heading = new JLabel("Secure Account Manager");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 21));
        heading.setForeground(TEXT);
        card.add(heading, c);

        JLabel intro = new JLabel("Account numbers are encrypted before being stored in MySQL.");
        intro.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        intro.setForeground(new Color(115, 127, 143));
        c.gridy++;
        card.add(intro, c);

        c.gridwidth = 1;
        c.gridy++;
        addField(card, c, "ACCOUNT HOLDER NAME", holderField);
        c.gridy++;
        addField(card, c, "BANK ACCOUNT NUMBER", accountField);
        c.gridy++;
        addField(card, c, "ACCOUNT BALANCE", balanceField);

        JLabel retrieveHeading = new JLabel("RETRIEVE SAVED ACCOUNT");
        retrieveHeading.setFont(new Font("Segoe UI", Font.BOLD, 11));
        retrieveHeading.setForeground(BLUE);
        c.gridy++;
        c.gridx = 0; c.gridwidth = 2;
        card.add(retrieveHeading, c);

        c.gridy++;
        c.gridwidth = 1;
        addField(card, c, "RECORD ID", idField);

        JPanel buttons = new JPanel(new GridLayout(1, 4, 10, 0));
        buttons.setOpaque(false);
        JButton saveButton = makeButton("Encrypt & Save", BLUE, Color.WHITE);
        JButton retrieveButton = makeButton("Retrieve & Decrypt", new Color(27, 139, 111), Color.WHITE);
        JButton clearButton = makeButton("Clear", new Color(232, 237, 243), TEXT);
        JButton deleteButton = makeButton("Delete", new Color(192, 57, 43), Color.WHITE);

        saveButton.addActionListener(e -> saveAccount());
        retrieveButton.addActionListener(e -> retrieveAccount());
        clearButton.addActionListener(e -> clearForm());
        deleteButton.addActionListener(e -> deleteAccount());

        buttons.add(saveButton);
        buttons.add(retrieveButton);
        buttons.add(clearButton);
        buttons.add(deleteButton);

        c.gridy++;
        c.gridx = 0; c.gridwidth = 2;
        c.insets = new Insets(20, 8, 8, 8);
        card.add(buttons, c);

        outer.add(card);
        return outer;
    }

    private void addField(JPanel panel, GridBagConstraints c, String label, JTextField field) {
        JPanel wrapper = new JPanel(new BorderLayout(0, 6));
        wrapper.setOpaque(false);

        JLabel caption = new JLabel(label);
        caption.setFont(new Font("Segoe UI", Font.BOLD, 10));
        caption.setForeground(new Color(100, 115, 133));

        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setForeground(TEXT);
        field.setBackground(new Color(249, 251, 253));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(218, 226, 235)),
                new EmptyBorder(10, 11, 10, 11)));

        wrapper.add(caption, BorderLayout.NORTH);
        wrapper.add(field, BorderLayout.CENTER);

        c.gridx = 0;
        c.gridwidth = 2;
        panel.add(wrapper, c);
    }

    private JButton makeButton(String text, Color background, Color foreground) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setForeground(foreground);
        button.setBackground(background);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(12, 10, 12, 10));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    private JPanel createFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(new Color(232, 238, 245));
        footer.setBorder(new EmptyBorder(12, 25, 12, 25));
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setForeground(new Color(75, 91, 110));
        footer.add(statusLabel, BorderLayout.WEST);

        JLabel cia = new JLabel("CONFIDENTIALITY  •  INTEGRITY  •  AVAILABILITY");
        cia.setFont(new Font("Segoe UI", Font.BOLD, 9));
        cia.setForeground(new Color(87, 111, 137));
        footer.add(cia, BorderLayout.EAST);
        return footer;
    }

    private void saveAccount() {
        try {
            String holder = holderField.getText().trim();
            String number = accountField.getText().trim();
            String balanceText = balanceField.getText().trim();

            if (holder.isEmpty() || number.isEmpty() || balanceText.isEmpty()) {
                showError("Please fill in all account fields.");
                return;
            }
            if (!number.matches("[0-9]{6,24}")) {
                showError("Account number must contain 6 to 24 digits.");
                return;
            }

            BigDecimal balance = new BigDecimal(balanceText);
            if (balance.signum() < 0) {
                showError("Balance cannot be negative.");
                return;
            }

            AccountDTO account = new AccountDTO(holder, number, balance);
            int id = accountDAO.save(account);
            idField.setText(String.valueOf(id));
            statusLabel.setText("Saved securely. Your record ID is " + id + ".");
            JOptionPane.showMessageDialog(this,
                    "Account encrypted and saved successfully.\nRecord ID: " + id,
                    "SecureBank", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            showError("Please enter a valid numeric balance, e.g. 2500.00");
        } catch (Exception ex) {
            showError("Could not save account: " + ex.getMessage());
        }
    }

    private void retrieveAccount() {
        try {
            int id = Integer.parseInt(idField.getText().trim());
            AccountDTO account = accountDAO.findById(id);

            if (account == null) {
                showError("No account found for record ID " + id + ".");
                return;
            }

            holderField.setText(account.getHolderName());
            accountField.setText(account.getAccountNumber());
            balanceField.setText(account.getBalance().toPlainString());
            statusLabel.setText("Integrity verified. Account decrypted successfully.");
        } catch (NumberFormatException ex) {
            showError("Enter a valid numeric Record ID.");
        } catch (SecurityException ex) {
            showError(ex.getMessage());
        } catch (Exception ex) {
            showError("Could not retrieve account: " + ex.getMessage());
        }
    }

    private void deleteAccount() {
        try {
            int id = Integer.parseInt(idField.getText().trim());

            int choice = JOptionPane.showConfirmDialog(this,
                    "Delete record ID " + id + " permanently?",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
            if (choice != JOptionPane.YES_OPTION) {
                return;
            }

            if (accountDAO.deleteById(id)) {
                clearForm();
                statusLabel.setText("Record ID " + id + " deleted successfully.");
                JOptionPane.showMessageDialog(this,
                        "Record ID " + id + " deleted successfully.",
                        "SecureBank", JOptionPane.INFORMATION_MESSAGE);
            } else {
                showError("No account found for record ID " + id + ".");
            }
        } catch (NumberFormatException ex) {
            showError("Enter a valid numeric Record ID.");
        } catch (Exception ex) {
            showError("Could not delete account: " + ex.getMessage());
        }
    }

    private void clearForm() {
        holderField.setText("");
        accountField.setText("");
        balanceField.setText("");
        idField.setText("");
        statusLabel.setText("Form cleared. Ready for a new account.");
    }

    private void showError(String message) {
        statusLabel.setText("Please check the entered information.");
        JOptionPane.showMessageDialog(this, message, "SecureBank",
                JOptionPane.ERROR_MESSAGE);
    }
}
