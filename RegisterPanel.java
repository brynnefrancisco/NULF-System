import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class RegisterPanel extends JPanel {

    private AppFrame frame;

    private JTextField firstNameField, lastNameField, middleInitialField, suffixField;
    private JTextField emailField, studentIdField, programField, sectionField;
    private JPasswordField passwordField;
    private JComboBox<String> monthBox, dayBox, yearBox, yearLevelBox;
    private JRadioButton femaleRadio, maleRadio;
    private ButtonGroup genderGroup;

    public RegisterPanel(AppFrame frame) {

        this.frame = frame;

        setLayout(null);
        setBackground(new Color(0x54, 0x77, 0x92));
        setBounds(0, 0, 1150, 680);

        JLabel titleLabel = new JLabel("Register", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setBounds(425, 10, 300, 40);
        add(titleLabel);

        JPanel cardPanel = new JPanel() {
            private static final int RADIUS = 25;

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setClip(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), RADIUS, RADIUS));
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), RADIUS, RADIUS);
                g2.dispose();
            }

            @Override
            protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(new Color(0, 0, 0, 40));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, RADIUS, RADIUS);
                g2.dispose();
            }
        };

        cardPanel.setOpaque(false);
        cardPanel.setLayout(null);
        cardPanel.setBackground(new Color(204, 210, 214));

        int cardWidth = 480;
        int cardHeight = 540;
        int cardX = (1150 - cardWidth) / 2;
        int cardY = (680 - cardHeight) / 2;
        cardPanel.setBounds(cardX, cardY, cardWidth, cardHeight);

        JLabel cardTitle = new JLabel("Create an account", SwingConstants.CENTER);
        cardTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        cardTitle.setForeground(new Color(0x4A, 0x70, 0xA9));
        cardTitle.setBounds(0, 15, cardWidth, 30);

        JLabel subtitle = new JLabel("It's quick and easy.", SwingConstants.CENTER);
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(Color.GRAY);
        subtitle.setBounds(0, 45, cardWidth, 20);

        // Create fields
        firstNameField = createField("First name", 30, 75, 200, false);

        lastNameField = createField("Last name", 250, 75, 200, false);

        middleInitialField = createField("Middle initial", 30, 130, 200, true);
        ((AbstractDocument) middleInitialField.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String text, AttributeSet attr) throws BadLocationException {
                if (text != null && fb.getDocument().getLength() < 1) {
                super.insertString(fb, offset, text.toUpperCase().substring(0, 1), attr);
            }
        }
        
        @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                    throws BadLocationException {
                if (text != null) {
                    String newText = text.toUpperCase();
                    if (fb.getDocument().getLength() - length + newText.length() <= 1) {
                        super.replace(fb, offset, length, newText, attrs);
                    }
                }
            }
        });

        suffixField = createField("Suffix", 250, 130, 200, true);

        monthBox = new JComboBox<>(new String[]{
                "January","February","March","April","May","June",
                "July","August","September","October","November","December"
        });
        monthBox.setBounds(30, 185, 120, 35);

        dayBox = new JComboBox<>();
        for (int i = 1; i <= 31; i++) dayBox.addItem(String.valueOf(i));
        dayBox.setBounds(160, 185, 90, 35);

        yearBox = new JComboBox<>();
        for (int y = 2026; y >= 1900; y--) yearBox.addItem(String.valueOf(y));
        yearBox.setBounds(260, 185, 90, 35);

        femaleRadio = new JRadioButton("Female");
        maleRadio = new JRadioButton("Male");
        femaleRadio.setBounds(30, 230, 100, 25);
        maleRadio.setBounds(150, 230, 100, 25);

        genderGroup = new ButtonGroup();
        genderGroup.add(femaleRadio);
        genderGroup.add(maleRadio);

        emailField = createField("Email - @nu-moa.edu.ph", 30, 265, 420, false);

        passwordField = new JPasswordField();
        passwordField.setBorder(BorderFactory.createTitledBorder("New password"));
        passwordField.setBounds(30, 315, 420, 40);

        ((AbstractDocument) passwordField.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                    throws BadLocationException {
                if (fb.getDocument().getLength() + text.length() <= 12) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });

        studentIdField = createField("Student ID (YYYY-XXXXXXX)", 30, 365, 420, false);

        yearLevelBox = new JComboBox<>(new String[]{
                "1st Year","2nd Year","3rd Year","4th Year","5th Year+"
        });
        yearLevelBox.setBounds(30, 415, 100, 35);

        programField = createField("Program", 140, 415, 150, true);
        sectionField = createField("Section", 300, 415, 150, true);

        JButton registerButton = new JButton("Register");
        registerButton.setBounds(140, 465, 200, 40);
        registerButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        registerButton.setBackground(new Color(0x4A, 0x70, 0xA9));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
        registerButton.addActionListener(e -> registerUser());

        JLabel loginLink = new JLabel("Already have an account?", SwingConstants.CENTER);
        loginLink.setBounds(140, 510, 200, 25);
        loginLink.setForeground(new Color(0x4A, 0x70, 0xA9));
        loginLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginLink.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                frame.showLogin();
            }
        });

        // Add components to card panel
        cardPanel.add(cardTitle);
        cardPanel.add(subtitle);
        cardPanel.add(firstNameField);
        cardPanel.add(lastNameField);
        cardPanel.add(middleInitialField);
        cardPanel.add(suffixField);
        cardPanel.add(monthBox);
        cardPanel.add(dayBox);
        cardPanel.add(yearBox);
        cardPanel.add(femaleRadio);
        cardPanel.add(maleRadio);
        cardPanel.add(emailField);
        cardPanel.add(passwordField);
        cardPanel.add(studentIdField);
        cardPanel.add(yearLevelBox);
        cardPanel.add(programField);
        cardPanel.add(sectionField);
        cardPanel.add(registerButton);
        cardPanel.add(loginLink);

        add(cardPanel);
    }

    // Modified createField with uppercase option
    private JTextField createField(String title, int x, int y, int width, boolean uppercase) {
        JTextField field = new JTextField();
        field.setBorder(BorderFactory.createTitledBorder(title));
        field.setBounds(x, y, width, 40);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        if (uppercase) {
            DocumentFilter uppercaseFilter = new DocumentFilter() {
                @Override
                public void insertString(FilterBypass fb, int offset, String text, AttributeSet attr) throws BadLocationException {
                    if (text != null) {
                        super.insertString(fb, offset, text.toUpperCase(), attr);
                    }
                }

                @Override
                public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                    if (text != null) {
                        super.replace(fb, offset, length, text.toUpperCase(), attrs);
                    }
                }
            };
            ((AbstractDocument) field.getDocument()).setDocumentFilter(uppercaseFilter);
        }

        return field;
    }

    private void registerUser() {

    String firstName = firstNameField.getText().trim();
    String lastName = lastNameField.getText().trim();
    String middleInitial = middleInitialField.getText().trim();
    String suffix = suffixField.getText().trim();

    String birthdate = monthBox.getSelectedItem() + " "
            + dayBox.getSelectedItem() + ", "
            + yearBox.getSelectedItem();

    String gender = femaleRadio.isSelected() ? "Female"
            : (maleRadio.isSelected() ? "Male" : "");

    String email = emailField.getText().trim();
    String password = new String(passwordField.getPassword());
    String studentId = studentIdField.getText().trim();
    String yearLevel = (String) yearLevelBox.getSelectedItem();
    String program = programField.getText().trim();
    String section = sectionField.getText().trim();

    if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty()
            || password.isEmpty() || studentId.isEmpty()
            || gender.isEmpty() || program.isEmpty() || section.isEmpty()) {

        JOptionPane.showMessageDialog(
                this,
                "Please fill in all required fields.",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE
        );
        return;
    }

    if (!email.toLowerCase().endsWith("@nu-moa.edu.ph")) {
        JOptionPane.showMessageDialog(
                this,
                "Please enter a valid school email ending with @nu-moa.edu.ph.",
                "Invalid Email",
                JOptionPane.ERROR_MESSAGE
        );
        return;
    }

    if (!studentId.matches("\\d{4}-\\d{1,7}")) {
        JOptionPane.showMessageDialog(
                this,
                "Student ID must follow format: YYYY-XXXXXXX (e.g. 2025-1234567)",
                "Invalid Student ID",
                JOptionPane.WARNING_MESSAGE
        );
        return;
    }

    boolean success = Database.registerUser(
            firstName, lastName, middleInitial, suffix,
            birthdate, gender, email, password,
            studentId, yearLevel, program, section
    );

    if (success) {
        JOptionPane.showMessageDialog(
                this,
                "Registration submitted.\nWaiting for admin approval.",
                "Success",
                JOptionPane.INFORMATION_MESSAGE
        );
        frame.showLogin();
    }
}

     private JTextField createField(String title, int x, int y, int width) {
        JTextField field = new JTextField();
        field.setBorder(BorderFactory.createTitledBorder(title));
        field.setBounds(x, y, width, 45);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return field;
    }
}
