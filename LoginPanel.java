import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class LoginPanel extends JPanel {

    private AppFrame frame;
    private JTextField emailField;
    private JPasswordField passwordField;

    public LoginPanel(AppFrame frame) {
        this.frame = frame;

        setLayout(null);
        setBackground(new Color(0x54, 0x77, 0x92));
        setBounds(0, 0, 1150, 680);

        JLabel titleLabel = new JLabel("NU MOA: Lost & Found");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 42));
        titleLabel.setBounds(80, 265, 600, 60);

        JLabel descriptionLabel = new JLabel(
                "<html>A centralized platform designed to efficiently report, track, and recover lost items within NU MOA.</html>");
        descriptionLabel.setForeground(Color.WHITE);
        descriptionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        descriptionLabel.setBounds(80, 290, 500, 100);

        add(titleLabel);
        add(descriptionLabel);

        JPanel cardPanel = new JPanel() {
            private static final int RADIUS = 25;

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), RADIUS, RADIUS);
                g2.dispose();
            }

            @Override
            protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.GRAY);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, RADIUS, RADIUS);
                g2.dispose();
            }
        };

        cardPanel.setOpaque(false);
        cardPanel.setLayout(null);
        cardPanel.setBackground(new Color(227, 227, 227));
        cardPanel.setBounds(600, 150, 450, 380);

        emailField = new JTextField();
        emailField.setBounds(40, 40, 370, 55);
        emailField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        emailField.setBorder(BorderFactory.createTitledBorder("Email"));

        passwordField = new JPasswordField();
        passwordField.setBounds(40, 110, 370, 55);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        passwordField.setBorder(BorderFactory.createTitledBorder("Password"));

        ((AbstractDocument) passwordField.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                    throws BadLocationException {
                int currentLength = fb.getDocument().getLength();
                int overLimit = (currentLength + text.length()) - 50 - length;
                if (overLimit > 0) {
                    text = text.substring(0, text.length() - overLimit);
                    JOptionPane.showMessageDialog(LoginPanel.this,
                            "Password cannot exceed 50 characters.", "Warning",
                            JOptionPane.WARNING_MESSAGE);
                }
                if (!text.isEmpty()) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });

        JButton loginButton = new JButton("Log In");
        loginButton.setBounds(40, 190, 370, 50);
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        loginButton.setBackground(new Color(0x8F, 0xAB, 0xD4));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.addActionListener(e -> loginUser());

        JSeparator separator = new JSeparator();
        separator.setBounds(40, 255, 370, 1);

        JButton registerButton = new JButton("Register");
        registerButton.setBounds(40, 290, 370, 50);
        registerButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        registerButton.setBackground(new Color(0x4A, 0x70, 0xA9));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
        registerButton.addActionListener(e -> frame.showRegister());

        cardPanel.add(emailField);
        cardPanel.add(passwordField);
        cardPanel.add(loginButton);
        cardPanel.add(separator);
        cardPanel.add(registerButton);

        add(cardPanel);
    }

    private void loginUser() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter email and password.",
                    "Login Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String result = Database.validateLogin(email, password);

        if ("PENDING".equals(result)) {
            JOptionPane.showMessageDialog(this,
                    "Your account is still pending admin approval.",
                    "Account Pending", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if ("INVALID".equals(result)) {
            JOptionPane.showMessageDialog(this, "Invalid email or password.",
                    "Login Failed", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this, "Login successful!");

        if ("ADMIN".equals(result)) {
            new AdminDashboard(frame, email).setVisible(true);
        } else {
            new HomePage(email).setVisible(true);
        }
        frame.dispose();
    }
}
