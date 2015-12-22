package second;

import java.util.*;
import java.io.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import static javafx.application.Platform.exit;


/**
 * Created by Администратор on 08.11.2015.
 */


public class RuletkaS {

    static HashMap<String, String> cacheUser = new HashMap<String, String>();
    enum errorMsg { BADPASS, NOTHINGLOGIN, OKUSER}


    public static void main(String[] args) throws TransformerException, ParserConfigurationException, IOException, SAXException {


        File file = new File("C:\\NewUser.xml");
        Document doc = openUserXML(file);
        loadXMLUserToArray(doc);


        System.out.println("Приложение рулетка");

        Scanner sc = new Scanner(System.in);

        System.out.println("Введите логин");
        String login, pass;
        login = sc.nextLine();
        System.out.println("Введите пароль");
        pass = sc.nextLine();

        System.out.println(login+"@"+pass);

        //if (FindUserInArray(login,pass,UserArray)==-1) WriteToXMLUser(login, pass, UserArray.length + 1, doc, file);
        switch (findUser(login, pass)){
            case NOTHINGLOGIN:
                cacheUser.put(login,pass);
                writeUser(new File("C:\\NewUser.xml"));
                break;
            case BADPASS:
                System.out.println("Неверный пароль");
                exit();
                break;
            case OKUSER:
                System.out.println("Продолжаем.....");
                break;
        }
        ;

        }


        //System.out.println(Integer.toString(FindUserInArray(login,pass,cacheUser)));

        public static errorMsg findUser(String login, String pass) {

        if (cacheUser.containsKey(login)){
            if (Objects.equals(cacheUser.get(login), pass)){
                return errorMsg.OKUSER;
            }
            else{
                System.out.println("Неверный пароль");
                return errorMsg.BADPASS;
            }
        }
        else{
            System.out.println("Несуществующий логин");
            return errorMsg.NOTHINGLOGIN;
        }

    }

    public static Document openUserXML(File file) throws IOException, SAXException, ParserConfigurationException {
        DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();
        DocumentBuilder db=dbf.newDocumentBuilder();
        Document OpenUserXML=db.parse(file);

        return OpenUserXML;
    }

    public static void loadXMLUserToArray(Document doc) throws IOException, SAXException, ParserConfigurationException {

        doc.getDocumentElement().normalize();
        NodeList userList=doc.getElementsByTagName("user");

        for(int i=0;i<userList.getLength();i++)
        {
            Node userNode=userList.item(i);

            if(userNode.getNodeType()==Node.ELEMENT_NODE) {

                Element userElem = (Element) userNode;
                NodeList loginNodeList = userElem.getElementsByTagName("login");
                Element elLoginNodeList = (Element) loginNodeList.item(0);
                NodeList login = elLoginNodeList.getChildNodes();
                NodeList passNodeList = userElem.getElementsByTagName("pass");
                Element elPassNodeList = (Element) passNodeList.item(0);
                NodeList pass = elPassNodeList.getChildNodes();
                System.out.println(
                        "[" + ((Node) login.item(0)).getNodeValue() + ", " + ((Node) pass.item(0)).getNodeValue() + "]"
                );

                cacheUser.put(((Node) login.item(0)).getNodeValue(), ((Node) pass.item(0)).getNodeValue());
            }

        }
    }

    public static void writeUser(File file) throws ParserConfigurationException, TransformerException {

        DocumentBuilder builder = null;
        builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = builder.newDocument();

        Element root = document.createElement("person");
        for(String login : cacheUser.keySet()){

            String value = cacheUser.get(login);

            Element newNode = document.createElement("user");
            Element newKey = document.createElement("login");
            Element newValue = document.createElement("pass");

            newKey.setTextContent(login);
            newValue.setTextContent(value);

            newNode.appendChild(newKey);
            newNode.appendChild(newValue);

            root.appendChild(newNode);

        }
        document.appendChild(root);


        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        Source source = new DOMSource(document);
        Result result = new StreamResult(file);
        transformer.transform(source,result);

     }
}
