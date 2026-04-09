import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

public class HamiltonDelivery {

    static String senderName = "";
    static String fromAddress = "";
    static String toAddress   = "";
    static String contact     = "";
    static String orderNotes  = "";
    static String category    = "Food";
    static double price       = 0.0;

    static final Color NAVY      = new Color(26, 58, 107);
    static final Color NAVY_LITE = new Color(42, 88, 160);
    static final Color BG        = new Color(245, 247, 250);
    static final Color CARD      = Color.WHITE;
    static final Color BORDER    = new Color(210, 215, 225);
    static final Color MUTED     = new Color(110, 120, 135);
    static final Color SUCCESS   = new Color(34, 139, 34);
    static final Color AMBER     = new Color(200, 140, 0);

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UIManager.put("Panel.background",       BG);
            UIManager.put("OptionPane.background",  BG);
            UIManager.put("TextField.background",   CARD);
            UIManager.put("TextArea.background",    CARD);
            new Screen1().setVisible(true);
        });
    }

    static class Screen1 extends JFrame {

        Screen1() {
            super("Hamilton Delivery — New Order");
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setSize(520, 560);
            setLocationRelativeTo(null);
            setResizable(false);
            getContentPane().setBackground(BG);

            JPanel root = new JPanel(new BorderLayout(0, 0));
            root.setBackground(BG);
            root.setBorder(new EmptyBorder(20, 24, 20, 24));

            root.add(buildHeader(), BorderLayout.NORTH);

            JPanel form = new JPanel(new GridBagLayout());
            form.setBackground(CARD);
            form.setBorder(new CompoundBorder(
                new LineBorder(BORDER, 1, true),
                new EmptyBorder(18, 20, 18, 20)
            ));

            GridBagConstraints gc = new GridBagConstraints();
            gc.fill      = GridBagConstraints.HORIZONTAL;
            gc.insets    = new Insets(5, 0, 5, 0);
            gc.gridwidth = GridBagConstraints.REMAINDER;

            JTextField tfName    = styledField("e.g. Sindi Leka");
            JTextField tfFrom    = styledField("Rruga e Dibres");
            JTextField tfTo      = styledField("Ali Demi");
            JTextField tfContact = styledField("sindileka@gmail.com");
            JTextArea  taNotes   = styledArea("Thigma description...", 4);

            addRow(form, gc, "Full name",       tfName);
            addRow(form, gc, "From address",    tfFrom);
            addRow(form, gc, "To address",      tfTo);
            addRow(form, gc, "Contact / email", tfContact);
            addRow(form, gc, "Order notes",     new JScrollPane(taNotes));

            root.add(form, BorderLayout.CENTER);

            JButton next = bigButton("Next →", NAVY);
            JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 12));
            btnPanel.setBackground(BG);
            btnPanel.add(next);
            root.add(btnPanel, BorderLayout.SOUTH);

            next.addActionListener(e -> {
                senderName = tfName.getText().trim();
                fromAddress = tfFrom.getText().trim();
                toAddress   = tfTo.getText().trim();
                contact     = tfContact.getText().trim();
                orderNotes  = taNotes.getText().trim();

                if (senderName.isEmpty() || fromAddress.isEmpty() || toAddress.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                        "Please fill in Name, From address and To address.",
                        "Required fields", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                new Screen2().setVisible(true);
                dispose();
            });

            add(root);
        }

        JPanel buildHeader() {
            JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
            p.setBackground(BG);
            p.setBorder(new EmptyBorder(0, 0, 16, 0));
            p.add(new LogoPanel());
            JPanel txt = new JPanel(new GridLayout(2, 1, 0, 2));
            txt.setBackground(BG);
            JLabel title = new JLabel("Hamilton Delivery");
            title.setFont(new Font("SansSerif", Font.BOLD, 18));
            title.setForeground(NAVY);
            JLabel sub = new JLabel("Postal & Courier Services");
            sub.setFont(new Font("SansSerif", Font.PLAIN, 12));
            sub.setForeground(MUTED);
            txt.add(title); txt.add(sub);
            p.add(txt);
            return p;
        }
    }

    static class Screen2 extends JFrame {

        static final String[] CATS  = {"Food", "Mail", "Clothing", "Misc."};
        static final double[] PRICES = {12.50, 4.99, 9.75, 7.00};
        static final String[] EMOJIS = {"🍱", "✉", "👕", "📦"};
        int selected = 0;

        JLabel priceLabel;
        CategoryImagePanel imgPanel;
        JPanel catBar;

        Screen2() {
            super("Hamilton Delivery — Choose Category");
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setSize(480, 480);
            setLocationRelativeTo(null);
            setResizable(false);
            getContentPane().setBackground(BG);

            JPanel root = new JPanel(new BorderLayout(0, 0));
            root.setBackground(BG);
            root.setBorder(new EmptyBorder(20, 24, 20, 24));

            JLabel title = new JLabel("Choose your delivery category");
            title.setFont(new Font("SansSerif", Font.BOLD, 16));
            title.setForeground(NAVY);
            title.setBorder(new EmptyBorder(0, 0, 14, 0));
            root.add(title, BorderLayout.NORTH);

            JPanel card = new JPanel(new BorderLayout(0, 12));
            card.setBackground(CARD);
            card.setBorder(new CompoundBorder(
                new LineBorder(BORDER, 1, true),
                new EmptyBorder(16, 18, 18, 18)
            ));

            catBar = new JPanel(new GridLayout(1, 4, 0, 0));
            catBar.setBackground(CARD);
            catBar.setBorder(new LineBorder(BORDER, 1, true));
            rebuildCatBar();
            card.add(catBar, BorderLayout.NORTH);

            imgPanel = new CategoryImagePanel(EMOJIS[selected], CATS[selected]);
            card.add(imgPanel, BorderLayout.CENTER);

            JPanel south = new JPanel(new BorderLayout(0, 8));
            south.setBackground(CARD);

            JPanel priceRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            priceRow.setBackground(CARD);
            JLabel lbl = new JLabel("Mailing price estimate: ");
            lbl.setFont(new Font("SansSerif", Font.PLAIN, 13));
            lbl.setForeground(MUTED);
            priceLabel = new JLabel(String.format("$%.2f", PRICES[selected]));
            priceLabel.setFont(new Font("SansSerif", Font.BOLD, 15));
            priceLabel.setForeground(SUCCESS);
            priceRow.add(lbl); priceRow.add(priceLabel);

            JButton submit = bigButton("Submit order", NAVY);
            submit.addActionListener(e -> {
                category = CATS[selected];
                price    = PRICES[selected];
                new Screen3().setVisible(true);
                dispose();
            });

            south.add(priceRow, BorderLayout.NORTH);
            south.add(submit,   BorderLayout.SOUTH);
            card.add(south, BorderLayout.SOUTH);

            root.add(card, BorderLayout.CENTER);
            add(root);
        }

        void rebuildCatBar() {
            catBar.removeAll();
            for (int i = 0; i < CATS.length; i++) {
                final int idx = i;
                JButton b = new JButton(CATS[i]);
                b.setFont(new Font("SansSerif", Font.PLAIN, 12));
                b.setFocusPainted(false);
                b.setBorder(BorderFactory.createMatteBorder(0, 0, 0, i < CATS.length-1 ? 1 : 0, BORDER));
                if (i == selected) {
                    b.setBackground(NAVY); b.setForeground(Color.WHITE);
                } else {
                    b.setBackground(CARD); b.setForeground(MUTED);
                }
                b.setOpaque(true);
                b.addActionListener(e -> {
                    selected = idx;
                    rebuildCatBar();
                    catBar.revalidate(); catBar.repaint();
                    imgPanel.update(EMOJIS[idx], CATS[idx]);
                    priceLabel.setText(String.format("$%.2f", PRICES[idx]));
                });
                catBar.add(b);
            }
        }
    }

    static class Screen3 extends JFrame {

        boolean productConfirmed = false;
        boolean priceConfirmed   = false;
        boolean opened = false;
        Timer animTimer;
        int    step = 0;
        JLabel statusLabel;
        DeliveryTrackPanel trackPanel;
        JButton btnProduct, btnPrice, btnOpen;

        Screen3() {
            super("Hamilton Delivery — Delivery Tracker");
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setSize(500, 560);
            setLocationRelativeTo(null);
            setResizable(false);
            getContentPane().setBackground(BG);

            JPanel root = new JPanel(new BorderLayout(0, 12));
            root.setBackground(BG);
            root.setBorder(new EmptyBorder(20, 24, 20, 24));

            JLabel title = new JLabel("Delivery Tracker");
            title.setFont(new Font("SansSerif", Font.BOLD, 16));
            title.setForeground(NAVY);
            root.add(title, BorderLayout.NORTH);

            JPanel centre = new JPanel();
            centre.setLayout(new BoxLayout(centre, BoxLayout.Y_AXIS));
            centre.setBackground(BG);

            JPanel sealPanel = sealedPanel();
            centre.add(sealPanel);
            centre.add(Box.createVerticalStrut(12));

            JPanel trackCard = new JPanel(new BorderLayout());
            trackCard.setBackground(CARD);
            trackCard.setBorder(new LineBorder(BORDER, 1, true));

            JPanel trackHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 8));
            trackHeader.setBackground(NAVY);
            JLabel hl = new JLabel("Delivery simulation");
            hl.setFont(new Font("SansSerif", Font.BOLD, 13));
            hl.setForeground(Color.WHITE);
            trackHeader.add(hl);
            trackCard.add(trackHeader, BorderLayout.NORTH);

            trackPanel = new DeliveryTrackPanel();
            trackCard.add(trackPanel, BorderLayout.CENTER);

            statusLabel = new JLabel("Awaiting order confirmation…");
            statusLabel.setFont(new Font("SansSerif", Font.ITALIC, 12));
            statusLabel.setForeground(MUTED);
            statusLabel.setBorder(new EmptyBorder(4, 12, 10, 12));
            trackCard.add(statusLabel, BorderLayout.SOUTH);

            centre.add(trackCard);
            centre.add(Box.createVerticalStrut(12));

            centre.add(summaryPanel());
            root.add(centre, BorderLayout.CENTER);

            JPanel buttons = new JPanel(new GridLayout(1, 3, 10, 0));
            buttons.setBackground(BG);

            btnOpen    = bigButton("Open order", new Color(70, 130, 60));
            btnPrice   = bigButton("Confirm price",   NAVY_LITE);
            btnProduct = bigButton("Confirm product", NAVY);

            btnPrice.setEnabled(false);
            btnProduct.setEnabled(false);

            btnOpen.addActionListener(e -> {
                opened = true;
                sealPanel.removeAll();
                JLabel ol = new JLabel("Order opened — delivery in progress");
                ol.setFont(new Font("SansSerif", Font.ITALIC, 12));
                ol.setForeground(SUCCESS);
                ol.setBorder(new EmptyBorder(8, 10, 8, 10));
                sealPanel.setBackground(new Color(230, 248, 230));
                sealPanel.add(ol);
                sealPanel.revalidate(); sealPanel.repaint();
                btnPrice.setEnabled(true);
                btnProduct.setEnabled(true);
                btnOpen.setEnabled(false);
                startAnimation();
            });

            btnPrice.addActionListener(e -> {
                priceConfirmed = true;
                btnPrice.setEnabled(false);
                btnPrice.setText("✓ Price confirmed");
                checkDone();
            });

            btnProduct.addActionListener(e -> {
                productConfirmed = true;
                btnProduct.setEnabled(false);
                btnProduct.setText("✓ Product confirmed");
                checkDone();
            });

            buttons.add(btnOpen);
            buttons.add(btnPrice);
            buttons.add(btnProduct);
            root.add(buttons, BorderLayout.SOUTH);

            add(root);
        }

        void startAnimation() {
            animTimer = new Timer(1800, null);
            animTimer.addActionListener(e -> {
                step++;
                trackPanel.setStep(step);
                String[] msgs = {
                    "Package picked up from sender…",
                    "In transit — on the way!",
                    "Out for final delivery…",
                    "Delivered successfully!"
                };
                if (step < msgs.length)
                    statusLabel.setText(msgs[step]);
                if (step >= 3) animTimer.stop();
            });
            animTimer.start();
        }

        void checkDone() {
            if (productConfirmed && priceConfirmed) {
                JOptionPane.showMessageDialog(this,
                    "Order fully confirmed!\nThank you for using Hamilton Delivery.",
                    "Order complete", JOptionPane.INFORMATION_MESSAGE);
            }
        }

        JPanel sealedPanel() {
            JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 8));
            p.setBackground(new Color(248, 243, 220));
            p.setBorder(new CompoundBorder(
                new LineBorder(new Color(200, 180, 100), 1, true),
                new EmptyBorder(2, 8, 2, 8)
            ));
            JLabel l = new JLabel("Order sealed — click 'Open order' to begin delivery");
            l.setFont(new Font("SansSerif", Font.ITALIC, 12));
            l.setForeground(new Color(130, 100, 0));
            p.add(l);
            return p;
        }

        JPanel summaryPanel() {
            JPanel p = new JPanel(new GridLayout(3, 2, 8, 4));
            p.setBackground(CARD);
            p.setBorder(new CompoundBorder(
                new LineBorder(BORDER, 1, true),
                new EmptyBorder(10, 14, 10, 14)
            ));
            addSummaryRow(p, "Recipient",  senderName.isEmpty() ? "—" : senderName);
            addSummaryRow(p, "Category",   category);
            addSummaryRow(p, "Price",      String.format("$%.2f", price));
            return p;
        }

        void addSummaryRow(JPanel p, String key, String val) {
            JLabel k = new JLabel(key);
            k.setFont(new Font("SansSerif", Font.PLAIN, 12));
            k.setForeground(MUTED);
            JLabel v = new JLabel(val);
            v.setFont(new Font("SansSerif", Font.BOLD, 12));
            v.setForeground(new Color(30, 30, 30));
            p.add(k); p.add(v);
        }
    }

    static class DeliveryTrackPanel extends JPanel {
        int currentStep = 0;
        static final String[] STEPS = {
            "Order received",
            "Picked up",
            "In transit",
            "Delivered"
        };
        static final String[] SUB = {
            "Hamilton HQ",
            "Sender's address",
            "On the road",
            "Destination reached"
        };

        DeliveryTrackPanel() {
            setBackground(CARD);
            setPreferredSize(new Dimension(420, 140));
        }

        void setStep(int s) { currentStep = s; repaint(); }

        @Override protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int n = STEPS.length;
            int w = getWidth(), h = getHeight();
            int xStep = w / n;
            int cy = h / 2 - 10;

            g2.setColor(BORDER);
            g2.setStroke(new BasicStroke(2));
            g2.drawLine(xStep / 2, cy, w - xStep / 2, cy);

            if (currentStep > 0) {
                g2.setColor(NAVY);
                int x1 = xStep / 2;
                int x2 = Math.min(xStep / 2 + (currentStep * xStep), w - xStep / 2);
                g2.drawLine(x1, cy, x2, cy);
            }

            for (int i = 0; i < n; i++) {
                int cx = xStep / 2 + i * xStep;

                int r = 12;
                if (i < currentStep) {
                    g2.setColor(NAVY);
                    g2.fillOval(cx - r, cy - r, r*2, r*2);
                    g2.setColor(Color.WHITE);
                    g2.setFont(new Font("SansSerif", Font.BOLD, 11));
                    g2.drawString("✓", cx - 5, cy + 5);
                } else if (i == currentStep) {
                    g2.setColor(AMBER);
                    g2.fillOval(cx - r, cy - r, r*2, r*2);
                } else {
                    g2.setColor(CARD);
                    g2.fillOval(cx - r, cy - r, r*2, r*2);
                    g2.setColor(BORDER);
                    g2.setStroke(new BasicStroke(2));
                    g2.drawOval(cx - r, cy - r, r*2, r*2);
                }

                g2.setColor(i <= currentStep ? NAVY : MUTED);
                g2.setFont(new Font("SansSerif", i == currentStep ? Font.BOLD : Font.PLAIN, 11));
                FontMetrics fm = g2.getFontMetrics();
                int tw = fm.stringWidth(STEPS[i]);
                g2.drawString(STEPS[i], cx - tw / 2, cy + r + 16);

                g2.setColor(MUTED);
                g2.setFont(new Font("SansSerif", Font.PLAIN, 10));
                fm = g2.getFontMetrics();
                tw = fm.stringWidth(SUB[i]);
                g2.drawString(SUB[i], cx - tw / 2, cy + r + 29);
            }
        }
    }

    static class CategoryImagePanel extends JPanel {
        String emoji, label;

        CategoryImagePanel(String emoji, String label) {
            this.emoji = emoji; this.label = label;
            setBackground(new Color(240, 244, 252));
            setPreferredSize(new Dimension(400, 110));
            setBorder(new MatteBorder(1, 0, 1, 0, BORDER));
        }

        void update(String e, String l) { emoji = e; label = l; repaint(); }

        @Override protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setFont(new Font("SansSerif", Font.PLAIN, 40));
            FontMetrics fm = g2.getFontMetrics();
            int ex = (getWidth() - fm.stringWidth(emoji)) / 2;
            g2.drawString(emoji, ex, 58);
            g2.setFont(new Font("SansSerif", Font.BOLD, 13));
            g2.setColor(NAVY);
            fm = g2.getFontMetrics();
            g2.drawString(label, (getWidth() - fm.stringWidth(label)) / 2, 82);
        }
    }

    static class LogoPanel extends JPanel {
        LogoPanel() {
            setPreferredSize(new Dimension(52, 52));
            setBackground(new Color(0, 0, 0, 0));
            setOpaque(false);
        }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(NAVY);
            g2.fillRoundRect(0, 0, 52, 52, 8, 8);
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(1.8f));
            g2.drawRoundRect(7, 16, 38, 24, 3, 3);
            g2.drawLine(7, 16, 26, 30);
            g2.drawLine(45, 16, 26, 30);
            g2.setFont(new Font("SansSerif", Font.BOLD, 6));
            FontMetrics fm = g2.getFontMetrics();
            String t = "HAMILTON";
            g2.drawString(t, (52 - fm.stringWidth(t)) / 2, 48);
        }
    }

    static JTextField styledField(String hint) {
        JTextField f = new JTextField(20);
        f.setFont(new Font("SansSerif", Font.PLAIN, 13));
        f.setForeground(MUTED);
        f.setBorder(new CompoundBorder(
            new LineBorder(BORDER, 1, true),
            new EmptyBorder(4, 8, 4, 8)
        ));
        f.setText(hint);
        f.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (f.getText().equals(hint)) { f.setText(""); f.setForeground(Color.BLACK); }
            }
            public void focusLost(FocusEvent e) {
                if (f.getText().isEmpty()) { f.setText(hint); f.setForeground(MUTED); }
            }
        });
        return f;
    }

    static JTextArea styledArea(String hint, int rows) {
        JTextArea a = new JTextArea(rows, 20);
        a.setFont(new Font("SansSerif", Font.PLAIN, 13));
        a.setForeground(MUTED);
        a.setLineWrap(true); a.setWrapStyleWord(true);
        a.setBorder(new EmptyBorder(6, 8, 6, 8));
        a.setText(hint);
        a.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (a.getText().equals(hint)) { a.setText(""); a.setForeground(Color.BLACK); }
            }
            public void focusLost(FocusEvent e) {
                if (a.getText().isEmpty()) { a.setText(hint); a.setForeground(MUTED); }
            }
        });
        return a;
    }

    static void addRow(JPanel p, GridBagConstraints gc, String labelText, JComponent field) {
        gc.gridwidth = GridBagConstraints.REMAINDER;
        gc.insets    = new Insets(2, 0, 1, 0);
        JLabel l = new JLabel(labelText);
        l.setFont(new Font("SansSerif", Font.PLAIN, 12));
        l.setForeground(MUTED);
        p.add(l, gc);
        gc.insets = new Insets(1, 0, 8, 0);
        p.add(field, gc);
    }

    static JButton bigButton(String text, Color bg) {
        JButton b = new JButton(text);
        b.setFont(new Font("SansSerif", Font.BOLD, 13));
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setOpaque(true);
        b.setPreferredSize(new Dimension(160, 36));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                b.setBackground(bg.darker());
            }
            public void mouseExited(MouseEvent e) {
                b.setBackground(bg);
            }
        });
        return b;
    }
}
