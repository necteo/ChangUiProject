package FoodNutrientManagement;

import org.json.simple.parser.ParseException;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTextField;

public class AutoSuggest extends JComboBox<String> {

    private final JTextField txtSug;
    private final Vector<String> foodList = new Vector<String>();
    private boolean layingOut = false;
    private int widestLength = 0;
    private boolean wide = false;
    private boolean hide_flag = false;

    public AutoSuggest() {
        setEditable(true);

        txtSug = (JTextField) getEditor().getEditorComponent();
        txtSug.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                EventQueue.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        if (e.paramString().contains("Backspace") || e.getKeyChar() == ' ') {
                            return;
                        }
                        setAutoSuggestComboBox();
                    }
                });
            }

            @Override
            public void keyPressed(KeyEvent e) {
                EventQueue.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        String text = txtSug.getText();
                        int code = e.getKeyCode();

                        if (code == KeyEvent.VK_ESCAPE) {
                            hide_flag = true;
                        } else if (code == KeyEvent.VK_ENTER) {
                            hide_flag = true;
                            for (int i = 0; i < foodList.size(); i++) {
                                String str = foodList.elementAt(i);
                                if (str.startsWith(text)) {
                                    txtSug.setText(str);
                                    break;
                                }
                            }
                            setAutoSuggestComboBox();
                        }
                    }
                });
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
    }

    @Override
    public void doLayout() {
        try {
            layingOut = true;
            super.doLayout();
        } finally {
            layingOut = false;
        }
    }

    @Override
    public Dimension getSize() {
        Dimension dim = super.getSize();
        if (!layingOut && isWide()) {
            setWide(true);
            dim.width = Math.max(widestLength, dim.width);
        }
        return dim;
    }

    private void setAutoSuggestComboBox() {
        String text = txtSug.getText();
        if (text.length() == 0) {
            hidePopup();
            setModel(new DefaultComboBoxModel<>(foodList));
            setSelectedIndex(-1);
        } else {
            try {
                ArrayList<String> tags = new ArrayList<String>();
                ArrayList<FoodNutrient> foods = GetOpenData.getData(text);
                if (foods != null) {
                    for (FoodNutrient tag : foods) {
                        tags.add(tag.getName());
                    }
                    foodList.removeAllElements();
                    for (String tag : tags) {
                        foodList.addElement(tag);
                    }
                }
            } catch (IOException | ParseException ex) {
                throw new RuntimeException(ex);
            }
            DefaultComboBoxModel<String> m = getSuggestedModel(foodList);
            if (m.getSize() == 0 || hide_flag) {
                hidePopup();
                hide_flag = false;
            } else {
                setModel(m);
                setSelectedIndex(-1);
                txtSug.setText(text);
                showPopup();
            }
        }
    }

    private static DefaultComboBoxModel<String> getSuggestedModel(Vector<String> fList) {
        DefaultComboBoxModel<String> cbm = new DefaultComboBoxModel<>();
        for (String food : fList) {
            cbm.addElement(food);
        }
        return cbm;
    }

    public boolean isWide() {
        return wide;
    }

    public int getWidestItemWidth() {
        int numOfItems = this.getItemCount();
        Font font = this.getFont();
        FontMetrics metrics = this.getFontMetrics(font);
        int widest = 0;
        for (int i = 0; i < numOfItems; i++) {
            Object item = this.getItemAt(i);
            int lineWidth = metrics.stringWidth(item.toString());
            widest = Math.max(widest, lineWidth);
        }
        return widest + 20;
    }

    /**
     *  넓은 드랍다운 리스트를 사용할 때. true를 주면 콤보박스보다 드랍다운리스트가 커짐.
     * @param wide true/false
     */
    public void setWide(boolean wide) {
        this.wide = wide;
        widestLength = getWidestItemWidth();
    }

    /**
     * 현재 입력되어있는 텍스트 반환
     * @return 현재 입력되어있는 텍스트 반환
     */
    public String getText() {
        return txtSug.getText();
    }
}