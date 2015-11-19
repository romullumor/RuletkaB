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

        HashMap<String, String> cacheUser = new HashMap<String, String>();

        File file = new File("C:\\user.xml");
        Document doc = OpenUserXML(file);
        LoadXMLUserToArray(doc, cacheUser);


        System.out.println("Приложение рулетка");

        Scanner sc = new Scanner(System.in);

        System.out.println("Введите логин");
        String login, pass;
        login = sc.nextLine();
        System.out.println("Введите пароль");
        pass = sc.nextLine();

        System.out.println(login+"@"+pass);

        //if (FindUserInArray(login,pass,UserArray)==-1) WriteToXMLUser(login, pass, UserArray.length + 1, doc, file);
        if (0 == FindUserInArray(login, pass, cacheUser)) {
            cacheUser.put(login,pass);
            System.out.println(convertToXML(cacheUser, "users"));

        };
        }


        //System.out.println(Integer.toString(FindUserInArray(login,pass,cacheUser)));

    }

    private static String convertToXML(HashMap<String, String> cache, String root) {
        StringBuilder sb = new StringBuilder("<");
        sb.append(root);
        sb.append(">");

        for (Map.Entry<String, String> e : cache.entrySet()) {
            sb.append("<item name='");
            sb.append(e.getKey());
            sb.append("' pass='");
            sb.append(e.getValue());
            sb.append("' />");
        }

        sb.append("</");
        sb.append(root);
        sb.append(">");

        return sb.toString();
    }

    private static int FindUserInArray(String login, String pass, HashMap<String, String> cache) {

        if (cache.containsKey(login)){
            if (Objects.equals(cache.get(login), pass)){
                return 1;
            }
            else{
                System.out.println("Неверный пароль");
                return -1;
            }
        }
        else{
            System.out.println("Несуществующий логин");
            return 0;
        }

    }

    private static Document OpenUserXML(File file) throws IOException, SAXException, ParserConfigurationException {
        DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();
        DocumentBuilder db=dbf.newDocumentBuilder();
        Document OpenUserXML=db.parse(file);

        return OpenUserXML;
    }

    public static void LoadXMLUserToArray(Document doc, HashMap<String, String> cache ) throws IOException, SAXException, ParserConfigurationException {

        doc.getDocumentElement().normalize();
        NodeList nodeLst=doc.getElementsByTagName("item");

        for(int je=0;je<nodeLst.getLength();je++)
        {
            Node fstNode=nodeLst.item(je);
            if(fstNode.getNodeType()==Node.ELEMENT_NODE)
            {
                Element elj=(Element)fstNode;
                cache.put(elj.getAttribute("pass"), elj.getAttribute("name"));
                System.out.println(elj.getAttribute("pass")+"===="+elj.getAttribute("name"));
            }
        }

    }


    private static void WriteToXMLUser(String login, String pass, int num, Document doc, File file) throws ParserConfigurationException, TransformerException {


       // DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
       // factory.setNamespaceAware(true);
       // Document document = factory.newDocumentBuilder().newDocument();

        Element root = doc.createElement("users");
        Element item = doc.createElement("item");

        item.setAttribute("name",login);
        item.setAttribute("pass",pass);
        item.setAttribute("id", Integer.toString(num+1));

        root.appendChild(item);

        doc.appendChild(root);

        Transformer transformer = TransformerFactory.newInstance().newTransformer();
       // transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(new DOMSource(doc), new StreamResult(file));
    }
}
