import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.FileWriter;

public class App {
    JPanel Main;
    private JButton donusturButton;
    private JRadioButton XMLJSONRadioButton;
    private JRadioButton JSONXMLRadioButton;
    private JButton ciktiyiKopyalaButton;
    private JTextArea inputBox;
    private JTextArea outputBox;

    public App() {
        donusturButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(inputBox.getText().isEmpty()){
                    JOptionPane.showMessageDialog(null,"Girdi kutusu boş.","Hata",JOptionPane.ERROR_MESSAGE);
                }
                else{
                    if(JSONXMLRadioButton.isSelected()){
                        int i = 0;
                        int numberofspaces = 0;
                        StringBuilder jsonInput = new StringBuilder();
                        String json = inputBox.getText();
                        while (true) {
                            if (json.length()==i) {
                                break;
                            }
                            char k = json.charAt(i);
                            if(k != '\n' && k != 9){
                                if(k == ' '){
                                    if(numberofspaces < 1){
                                        jsonInput.append(k);
                                        numberofspaces++;
                                    }
                                }
                                else {
                                    jsonInput.append(k);
                                    numberofspaces--;
                                }

                            }
                            i++;
                        }
                        try {
                            JSONObject jsonFromUser = new JSONObject(jsonInput.toString());
                            String rootName = (String) jsonFromUser.keys().next();
                            String xmlConverted = "<" + rootName + ">\n" +
                                    Converter.jsonToXml(jsonFromUser.getJSONObject(rootName)) +
                                    "</" + rootName + ">";

                            outputBox.setText(xmlConverted);
                            // XML çıktısını dosyaya yaz
                            FileWriter writer = new FileWriter("veri.xml");
                            writer.write(xmlConverted);
                            writer.close();

                        } catch (Exception h) {
                            JOptionPane.showMessageDialog(null,"Geçersiz JSON formatı! Hata: "+ h.getMessage(),"Hata",JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    else if(XMLJSONRadioButton.isSelected()) {
                        int i = 0;
                        int numberofspaces = 0;
                        String xml = inputBox.getText();
                        StringBuilder xmlBuilder = new StringBuilder();
                        while (true) {
                            if (xml.length()==i) {
                                break;
                            }
                            char k = xml.charAt(i);
                            if(k != '\n' && k != 9){
                                if(k == ' '){
                                    if(numberofspaces < 1){
                                        xmlBuilder.append(k);
                                        numberofspaces++;
                                }
                                }
                                else {
                                    xmlBuilder.append(k);
                                    numberofspaces--;
                                }

                            }
                            i++;
                        }

                        xml = xmlBuilder.toString();
                        if (!Converter.isXmlValid(xml)) {

                            JOptionPane.showMessageDialog(null,"XML formatı hatalı.","Hata",JOptionPane.ERROR_MESSAGE);
                        } else {
                            try {

                                Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                                        .parse(new ByteArrayInputStream(xml.getBytes()));
                                Element root = doc.getDocumentElement();

                                JSONObject json = new JSONObject();
                                json.put(root.getTagName(), Converter.xmlToJson(root));

                                outputBox.setText(json.toString());

                                // Dosyaya yaz
                                FileWriter writer = new FileWriter("veri.json");
                                writer.write(json.toString(4));
                                writer.close();
                                System.out.println("JSON başarıyla veri.json dosyasına yazıldı.");


                            } catch (Exception a) {
                                JOptionPane.showMessageDialog(null,a.getMessage(),"Hata",JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                    else{
                        JOptionPane.showMessageDialog(null,"Dönüşüm türünü seçmelisiniz.","Hata",JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        XMLJSONRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JSONXMLRadioButton.setSelected(false);
            }
        });
        JSONXMLRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                XMLJSONRadioButton.setSelected(false);
            }
        });

        ciktiyiKopyalaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(outputBox.getText().isEmpty()){
                    JOptionPane.showMessageDialog(null,"Çıktı kutusu boş.","Hata",JOptionPane.ERROR_MESSAGE);
                }
                else{
                    StringSelection stringSelection = new StringSelection(outputBox.getText());
                    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                    clipboard.setContents(stringSelection, null);
                    JOptionPane.showMessageDialog(null,"Çıktı kopyalandı.","Bilgi",JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
    }
}
