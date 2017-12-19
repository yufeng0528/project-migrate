package migrate.handler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import migrate.MigrateException;
import migrate.dto.Config;
import migrate.util.Constants;
import migrate.util.FileConstants;

public class PomReplaceHandler implements ReplaceHandler {

	private String basePath;

	private Config config;

	@Override
	public void handler(Config config, String basePath) {
		this.config = config;
		this.basePath = basePath;
		find();
	}

	private void find() {
		File baseDirectory = new File(basePath + "/" + config.getProject());
		Iterator<File> tagetFiles = FileUtils.iterateFiles(baseDirectory, new IOFileFilter() {

			@Override
			public boolean accept(File dir, String name) {
				return true;
			}

			@Override
			public boolean accept(File file) {
				if (file.getName().startsWith(".")) {
					return false;
				}
				String suffix = FilenameUtils.getExtension(file.getName());
				if (suffix.equals(FileConstants.XML_SUFFIX)) {
					File parentDir = file.getParentFile();
					if (parentDir.getName().equals(config.getDir())) {
						return true;
					}
					return false;
				}
				return false;
			}
		}, new TargetFileFilter());
		while (tagetFiles.hasNext()) {
			File file = (File) tagetFiles.next();

			System.out.println("--" + file.getAbsolutePath());
			try {
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				dbf.setIgnoringElementContentWhitespace(true);
				DocumentBuilder db = dbf.newDocumentBuilder();
				Document doc = db.parse(file);
				Element root = doc.getDocumentElement();
				
//				System.out.println(root.getTagName());
				
				Node node = selectSingleNode(config.getKey(), root);
				if (node == null) {
					throw new MigrateException(config.getKey() + " xpath路径有问题");
				}
				node.setTextContent(config.getValue());

//				System.out.println(node.getNodeName() + " " + node.getTextContent());
				
				TransformerFactory factory = TransformerFactory.newInstance();
				Transformer former = factory.newTransformer();
				former.transform(new DOMSource(doc), new StreamResult(file));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			} catch (TransformerConfigurationException e) {
				e.printStackTrace();
			} catch (TransformerException e) {
				e.printStackTrace();
			}

		}
	}

	public static Node selectSingleNode(String express, Element source) {
		Node result = null;
		XPathFactory xpathFactory = XPathFactory.newInstance();
		XPath xpath = xpathFactory.newXPath();
		try {
			result = (Node) xpath.evaluate(express, source, XPathConstants.NODE);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}

		return result;
	}

	public static void main(String[] args) {
		ReplaceHandler replaceHandler = new PomReplaceHandler();
		Config config = new Config();
		config.setDir("qiakr-mtfix-account-provider");
		config.setKey("/project/parent/groupId");
		config.setValue("com.yiguo.demo");
		config.setType("pom");
		config.setProject("mtfix-account-center");
		replaceHandler.handler(config, Constants.BASE_DIR);
	}

}
