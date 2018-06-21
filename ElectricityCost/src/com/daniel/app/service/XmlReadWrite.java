package com.daniel.app.service;

import com.daniel.app.data.Device;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class XmlReadWrite {

    public List<Device> readXmlFile(String path) {
        List<Device> deviceList = new ArrayList<>();
        try {
            File fXmlFile = new File(path);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            NodeList nList = doc.getElementsByTagName("device");
            for (int temp = 0; temp < nList.getLength(); temp++) {

                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;

                    String deviceName = eElement.getAttribute("name");
                    double devicePower = Double.parseDouble(eElement.getElementsByTagName("power").item(0).getTextContent());
                    double deviceTime = Double.parseDouble(eElement.getElementsByTagName("usingTime").item(0).getTextContent());
                    deviceList.add(new Device(deviceName, devicePower, deviceTime));
                }
            }

        } catch (Exception e) {

        }
        return deviceList;
    }

        public void saveXmlFile(List<Device> deviceList, String path){

            try {
                DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

                Document doc = docBuilder.newDocument();
                Element rootElement = doc.createElement("devices");
                doc.appendChild(rootElement);

                for(Device dev:deviceList) {
                    Element device = doc.createElement("device");
                    rootElement.appendChild(device);

                    device.setAttribute("name", dev.getName());

                    Element power = doc.createElement("power");
                    power.appendChild(doc.createTextNode("" + dev.getPower()));
                    device.appendChild(power);

                    Element usingTime = doc.createElement("usingTime");
                    usingTime.appendChild(doc.createTextNode("" + dev.getTime()));
                    device.appendChild(usingTime);
                }

                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(doc);
                StreamResult result = new StreamResult(new File(path));

                transformer.transform(source, result);
            }catch(Exception e){
            }

        }

    }

