package xmlextractor;

import com.sun.org.apache.xerces.internal.impl.xs.XSImplementationImpl;
import com.sun.org.apache.xerces.internal.xs.XSLoader;
import com.sun.org.apache.xerces.internal.xs.XSModel;
import org.apache.xmlbeans.*;
import org.apache.xmlbeans.impl.inst2xsd.Inst2Xsd;
import org.apache.xmlbeans.impl.inst2xsd.Inst2XsdOptions;
import org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource.Factory.Registry;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDParticle;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.XSDTypeDefinition;
import org.eclipse.xsd.impl.XSDModelGroupImpl;
import org.eclipse.xsd.util.XSDResourceFactoryImpl;
import org.eclipse.xsd.util.XSDResourceImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Anil Chowdhury
 *         Created on 11/12/2018.
 */
public class XmlSchemaExtraction {

    public static void main(String[] args) {
        XmlSchemaExtraction driver = new XmlSchemaExtraction();

        /*driver.generateXsd("C:\\files\\xml\\books.xml");
        driver.generateXsd("C:\\files\\xml\\breakfastMenu.xml");
        driver.generateXsd("C:\\files\\xml\\cars.xml");
        driver.generateXsd("C:\\files\\xml\\employee.xml");
        driver.generateXsd("C:\\files\\xml\\employeeAddress.xml");
        driver.generateXsd("C:\\files\\xml\\innerBooks.xml");
        driver.generateXsd("C:\\files\\xml\\note.xml");
        driver.generateXsd("C:\\files\\xml\\notesEmployee.xml");
        driver.generateXsd("C:\\files\\xml\\sampleXMLPerson.xml");
        driver.generateXsd("C:\\files\\xml\\simpleEmployee.xml");
        driver.generateXsd("C:\\files\\xml\\nodes.xml");*/

//      driver.printPath("C:\\files\\xml\\notesEmployee.xml");

        /*
        driver.eclipseXSD("C:\\files\\xml\\xsd\\books.xsd");
        driver.eclipseXSD("C:\\files\\xml\\xsd\\breakfastMenu.xsd");
        driver.eclipseXSD("C:\\files\\xml\\xsd\\cars.xsd");
        driver.eclipseXSD("C:\\files\\xml\\xsd\\employee.xsd");
        driver.eclipseXSD("C:\\files\\xml\\xsd\\innerBooks.xsd");
        driver.eclipseXSD("C:\\files\\xml\\xsd\\note.xsd");
        driver.eclipseXSD("C:\\files\\xml\\xsd\\notesEmployee.xsd");
        driver.eclipseXSD("C:\\files\\xml\\xsd\\sampleXMLPerson.xsd");
        driver.eclipseXSD("C:\\files\\xml\\xsd\\simpleEmployee.xsd");
        driver.eclipseXSD("C:\\files\\xml\\xsd\\nodes.xsd");
        driver.eclipseXSD("C:\\files\\xml\\xsd\\employeeAddress.xsd");
        */

        driver.printPath("C:\\files\\xml\\employeeAddress.xml");

//      driver.eclipseXSD("C:\\files\\xml\\xsd\\nodes.xsd");
//      driver.processUsingXmlBeans("C:\\files\\xml\\product.xsd");
//      driver.processUsingXSDModel("C:\\files\\xml\\simpleEmployee.xsd");

    }

    private void generateXsd(String filePath) {
        File file = new File(filePath);
        XmlObject[] xmlInstances = new XmlObject[1];
        try {
            xmlInstances[0] = XmlObject.Factory.parse(file);
            Inst2XsdOptions options = new Inst2XsdOptions();
            options.setUseEnumerations(1);
            options.setSimpleContentTypes(2);
            options.setDesign(Inst2XsdOptions.SIMPLE_CONTENT_TYPES_SMART);
            SchemaDocument[] schemaDocuments = Inst2Xsd.inst2xsd(xmlInstances, options);
            StringWriter writer = new StringWriter();

            /*for(SchemaDocument doc : schemaDocuments){
                doc.save(writer, new XmlOptions().setSavePrettyPrint());
            }*/
            process(xmlInstances, options);
            schemaDocuments[0].save(writer, new XmlOptions().setSavePrettyPrint());
            writer.close();
            String xmlText = writer.toString();
            System.out.println("-------------------------- " + file.getName() + " --------------------------");
            System.out.println(xmlText);
        } catch (XmlException | IOException e) {
            e.printStackTrace();
        }
    }

    private void printPath(String filePath) {
        Set<String> paths = new HashSet<>();
        File file = new File(filePath);
        XPath xPath =  XPathFactory.newInstance().newXPath();
        String expression = "//*[not(*)]";
        try{
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            Document document = builder.parse(file);
            document.getDocumentElement().normalize();

            NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(document, XPathConstants.NODESET);
            for(int i = 0 ; i < nodeList.getLength(); i++) {
//              System.out.println(getXPath(nodeList.item(i)));
                paths.add(getXPath(nodeList.item(i)));
            }
        } catch (SAXException | XPathExpressionException | ParserConfigurationException | IOException e) {
            e.printStackTrace();
        }
        System.out.println("-------------------------- " + file.getName() + " --------------------------");
        paths.forEach(System.out::println);
    }

    private static String getXPath(Node node) {
        Node parent = node.getParentNode();
        if (parent == null) {
            return node.getNodeName();
        }
        return getXPath(parent) + "/" + node.getNodeName();
    }

    private void eclipseXSD(String xsdPath) {
        File locationFile = new File(xsdPath);
        URI fileURI = URI.createFileURI(locationFile.getAbsolutePath());
        XSDResourceFactoryImpl resourceFactory = new XSDResourceFactoryImpl();
        Registry.INSTANCE.getExtensionToFactoryMap().put("xsd", resourceFactory);
        ResourceSetImpl resourceSet = new ResourceSetImpl();
        resourceSet.getLoadOptions().put(XSDResourceImpl.XSD_TRACK_LOCATION, Boolean.TRUE);
        XSDResourceImpl xsdSchemaResource = (XSDResourceImpl)resourceSet.getResource(fileURI, true);
        XSDSchema schema = xsdSchemaResource.getSchema();
        XSDElementDeclaration xsdElementDeclaration = schema.getElementDeclarations().get(0);
        List<String> flattenPath = flattenAllElements(xsdElementDeclaration);

        System.out.println("-------------------------- " + locationFile.getName() + " --------------------------");
        flattenPath.forEach(System.out::println);
    }

    private List<String> flattenAllElements(XSDElementDeclaration xsdElementDeclaration) {
        List<String> children = new ArrayList<>();
        String parentName = xsdElementDeclaration.getName();
        XSDTypeDefinition typeDefinition = xsdElementDeclaration.getTypeDefinition();
        /*String nodeName = typeDefinition.getElement().getNodeName();
        boolean isComplex = nodeName.equals("xs:complexType");*/
        boolean isComplex = typeDefinition.getComplexType() != null;
        if (isComplex) {
            EList<XSDParticle> contents = ((XSDModelGroupImpl) typeDefinition.getComplexType().getContent()).getContents();
            for (XSDParticle particle : contents) {
                XSDElementDeclaration content = (XSDElementDeclaration) particle.getContent();
                children.addAll(flattenAllElements(content).stream().map(child -> "/" + parentName + child).collect(Collectors.toList()));
            }
        } else {
            children.add("/" + parentName);
        }
        return children;
    }

    private void processUsingXmlBeans(String xsdFile) {
        try {
            SchemaTypeSystem sts = XmlBeans.compileXsd(new XmlObject[] {
                    XmlObject.Factory.parse(xsdFile) }, XmlBeans.getBuiltinTypeSystem(), null);
            System.out.println(sts.toString());
        } catch (XmlException e) {
            e.printStackTrace();
        }
    }

    private void processUsingXSDModel (String xsd) {
        try {
            System.setProperty(DOMImplementationRegistry.PROPERTY, "com.sun.org.apache.xerces.internal.dom.DOMXSImplementationSourceImpl");
            DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();
            XSImplementationImpl impl = (XSImplementationImpl) registry.getDOMImplementation("XS-Loader");
            XSLoader schemaLoader = impl.createXSLoader(null);
            XSModel model = schemaLoader.loadURI(xsd);
            System.out.println(model.toString());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void process(XmlObject[] xmlInstances, Inst2XsdOptions options) {
        XmlCursor cursor = xmlInstances[0].newCursor();
        while(!cursor.isStart()) {
            cursor.toNextToken();
        }
        System.out.println("***********************************************************");
        while (!cursor.toNextToken().isNone()) {
            switch (cursor.currentTokenType().intValue()) {
                case XmlCursor.TokenType.INT_START:
                    // Print out the token type and a message.
                    System.out.println(cursor.currentTokenType().toString() + " " + cursor.getName() +
                            "; cursor is at the start of an element.");
                    break;
            }
        }
    }

    private void processXSD(String filePath){
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder;
        Document doc;
        try {
            System.out.println("***********************************************************");
            docBuilder = docBuilderFactory.newDocumentBuilder();
            doc = docBuilder.parse (new File(filePath));
            NodeList list = doc.getElementsByTagName("xs:element");
            for(int i = 0 ; i < list.getLength(); i++) {
                Element first = (Element)list.item(i);

                if(first.hasAttributes()) {
                    String nm = first.getAttribute("name");
                    System.out.println(nm + " --> ");
                    String nm1 = first.getAttribute("type");
                    System.out.println(nm1);
                }
                NodeList childNodes = first.getChildNodes();
                System.out.println(childNodes.toString());
            }
        } catch (SAXException | IOException | ParserConfigurationException e) {
            e.printStackTrace();
        }
    }
}

