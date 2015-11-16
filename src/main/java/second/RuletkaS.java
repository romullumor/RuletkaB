package second;

import java.util.*;
import java.io.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document; // обратите внимание !
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


/**
 * Created by Администратор on 08.11.2015.
 */


public class RuletkaS {
    public static void main(String[] args) throws TransformerException, ParserConfigurationException, IOException, SAXException {

        File file = new File("C:\\user.xml");
        Document doc;
        doc = OpenUserXML(file);
        String[][] UserArray = LoadXMLUserToArray(doc);


        System.out.println("Приложение рулетка");

        Scanner sc = new Scanner(System.in);

        System.out.println("Введите логин");
        String login, pass;
        login = sc.nextLine();
        System.out.println("Введите пароль");
        pass = sc.nextLine();

        System.out.println(login+"@"+pass);

        if (FindUserInArray(login,pass,UserArray)==-1) WriteToXMLUser(login, pass, UserArray.length + 1, doc, file);

    }

    private static int FindUserInArray(String login, String pass, String[][] userArray) {
        for (int i = 0; i < userArray.length ; i++) {
            if (userArray[i][1] == login) {
                if (userArray[i][0] == pass) {
                    return i;

                } else {
                    System.out.println("Неверный пароль");
                    return -2;
                }
            } else {
                System.out.println("Несуществующий логин");
                return -1;
            }
        }
        return -1;
    }

    private static Document OpenUserXML(File file) throws IOException, SAXException, ParserConfigurationException {
        DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();
        DocumentBuilder db=dbf.newDocumentBuilder();
        Document OpenUserXML=db.parse(file);

        return OpenUserXML;
    }

    private static String[][] LoadXMLUserToArray(Document doc) throws IOException, SAXException, ParserConfigurationException {

        doc.getDocumentElement().normalize();

        NodeList nodeListID = doc.getElementsByTagName("userid");
        Node IdNode = nodeListID.item(0);
        Element elid=(Element)IdNode;
        int idlist = Integer.parseInt(elid.getAttribute("id"));
        String [][] baseuser = new String[idlist][2];

        doc.getDocumentElement().normalize();
        NodeList nodeLst=doc.getElementsByTagName("item");

        for(int je=0;je<nodeLst.getLength();je++)
        {
            Node fstNode=nodeLst.item(je);
            if(fstNode.getNodeType()==Node.ELEMENT_NODE)
            {
                Element elj=(Element)fstNode;
                baseuser[je][0] = elj.getAttribute("name");
                baseuser[je][1] = elj.getAttribute("pass");
                System.out.println(elj.getAttribute("pass")+"===="+elj.getAttribute("name"));
            }
        }
        return baseuser;
    }




    private static void WriteToXMLUser(String login, String pass, int num, Document doc, File file) throws ParserConfigurationException, TransformerException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        Document document = factory.newDocumentBuilder().newDocument();

        Element root = doc.createElement("users");
        Element item = doc.createElement("item");

        item.setAttribute("name",login);
        item.setAttribute("pass",pass);
        item.setAttribute("id", Integer.toString(num));

        root.appendChild(item);

        Element userid = document.createElement("userid");
        userid.setAttribute("id", Integer.toString(num));
        root.appendChild(userid);

        doc.appendChild(root);

        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(new DOMSource(doc), new StreamResult(file));
    }
}
