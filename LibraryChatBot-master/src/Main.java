import java.util.*;
import java.io.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Main extends JFrame implements KeyListener {

    JPanel p = new JPanel();
    JTextArea replyArea = new LibraryChatBot(35, 89);
    JTextArea message = new JTextArea(1, 89);
    JScrollPane scrollable = new JScrollPane(replyArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    JButton button = new JButton("Send");

    public void keyReleased(KeyEvent e) {}

    public void keyPressed(KeyEvent e) {}

    public void keyTyped(KeyEvent e) {}

    public java.util.List<Element> readXmlFile(String file){
        java.util.List<Element> answers = new ArrayList();

        try{
            File xmlFileName= new File("Answers.xml");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(xmlFileName);

            document.getDocumentElement().normalize();
            NodeList nodes = document.getElementsByTagName(file);

            for(int temp = 0 ; temp < nodes.getLength();temp++){
                Node tempNode = nodes.item(temp);

                if(tempNode.getNodeType() == Node.ELEMENT_NODE){
                    Element element = (Element) tempNode;
                    answers.add(element);
                }
            }
        }catch(Exception e){
            System.out.println("Error:" +e);
        }
        return answers;
    }

    public void addText(String str) {
        replyArea.setText(replyArea.getText() + str);
    }

    String[] keywords = {
            "\\byazari kimdir|kimdir|kim yazdi|yazdi|kime ait|ait|kim tarafindan\\b",
            "kitapYazar",
            "\\bzaman|tarih|yil|zaman\\b",
            "kitapTarih",
            "\\bsayfa|sayfadir|sayfadan\\b",
            "kitapSayfa",
            "\\bfiyat|ucret|ne kadar|kadar|mebla|tutar|para\\b",
            "kitapFiyat",
            "\\bhakkinda|bilgi^[a-z]|alabilir\\b",
            "kitapInfo",
            "\\bonerir|tavsiye|tur|okumalı^[a-z]\\b",
            "kitapOneri",
            "\\ben cok|cok|fazla|en fazla|ilgi|ilgi goren|satan\\b",
            "kitapMostSeller",
            "^[a-zA-Z0-9_.-]*$",
            "message"
    };

    public void sendMessage(){
        String recievedText = message.getText();
        message.setText("");
        addText("User: " + recievedText);
        recievedText.trim();

        byte response = 0;
        int j = 0;
        while (response == 0) {
            Pattern pattern = Pattern.compile(keywords[j * 2]);
            Matcher matcher = pattern.matcher(recievedText.trim());
            String answer="";

            if (matcher.find()) {
                response = 2;
                java.util.List<Element> result =readXmlFile(keywords[(j*2)+1]);

                for(Element element : result){
                    if((recievedText.indexOf(element.getAttribute("name")) != -1)){
                        answer = element.getElementsByTagName("answer").item(0).getTextContent();
                        break;
                    }
                    else if((recievedText.indexOf(element.getAttribute("name")) == -1)){
                        java.util.List<Element> message =readXmlFile("message");
                        for(Element temp2 : message){
                            answer = temp2.getElementsByTagName("notUnderstand").item(0).getTextContent();

                        }
                    }

                }
                addText("\nLibraryRobot: " + answer);
            }


            j++;
            if (j * 2 == keywords.length - 1 && response == 0) {
                response = 1;
            }
        }

        if (response == 1) {
            addText("\nLibraryRobot: " + keywords[keywords.length - 1]);
        }
        addText("\n");
    }

    public Main(){
        super("Library Chat Bot");
        setSize(1050, 750);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        p.add(scrollable);
        p.add(message);
        p.add(button);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        add(p);

        setVisible(true);
    }

    /*String[] keywords = {
            "\\bkimdir\\b",
            "kitapAd",
            "\\byazdi\\b",
            "kitapAd",
            "\\bzaman\\b",
            "kitapDate",
            "^(?=.*\\bhangi) (?=.*\\btarih) (?=.*\\byil).*$",
            "kitapDate",
            "\\ben cok\\b",
            "kitapBest",
            "\\bsatan\\b",
            "kitapBest",
            "\\bdizi\\b",
            "kitapDizi",
            "\\bfilm\\b",
            "kitapFilm",
            "\\bsayfa\\b",
            "kitapSayfa",
            "\\boner\\b",
            "kitapOneri",
            "\\bson yazdigi\\b",
            "kitapSon",
            "\\bserisinin\\b",
            "kitapSeriSon",
            "\\bTurkiye'de bestseller\\b",
            "kitapSatan",
            "\\betkilesim\\b",
            "kitapSosyal",
            "\\bsiparis\\b",
            "kitapSiparis",
            "\\boldukten yayinlanan\\b",
            "kitapOlum",
            "\\beski\\b",
            "kitapOld",
            "^[a-zA-Z0-9_.-]*$",
            "message"
    };*/

    public static void main(String[] args){
        new Main();
    }
}
