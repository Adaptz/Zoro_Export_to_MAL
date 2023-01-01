import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public class Main {
    public static void main(String[] args) throws Exception {
        // creates a File object representing the "export.xml" file
        File xmlFile = new File("export.xml");
        
        // creates a new DocumentBuilderFactory
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        
        // creates a new DocumentBuilder using the DocumentBuilderFactory
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        
        // parses the xmlFile and creates a new Document object
        Document doc = dBuilder.parse(xmlFile);
        
        // normalizes the document
        doc.getDocumentElement().normalize();
        
        // gets all the elements in the document and stores them in a NodeList
        NodeList nodeList = doc.getElementsByTagName("*");
        
        // creates a new list to store animes
        List<String> animedbID = new ArrayList<>();
        List<String> names = new ArrayList<>();
        List<String> completedAnime = new ArrayList<>();
        List<String> onHoldAnime = new ArrayList<>();
        List<String> watchingAnime = new ArrayList<>();
        List<String> planToWatchAnime = new ArrayList<>();
        List<String> droppedAnime = new ArrayList<>();

        
        // iterates through the NodeList
        for (int i = 0; i < nodeList.getLength(); i++) {
            // gets the current node
            Node node = nodeList.item(i);
            // checks if the node name is "name"
            if (node.getNodeName().equals("name")) 
            {
                // if it is, adds the node's text content to the names list
                names.add(node.getTextContent());
            }
            if (node.getNodeName().equals("link"))
            {
                String link = node.getTextContent();
                String[] linkParts = link.split("/");
                String ID = linkParts[linkParts.length - 1];
                animedbID.add(ID);
            }
        }
        
        // flag variable to track when "On-Hold" is encountered
        boolean onHold = false;
        boolean watching = false;
        boolean planToWatch = false;
        boolean dropped = false;

        // iterates through the names list
        for (String name : names) 
        {
            // checks if the flag is set to true
            if(dropped)
            {
                droppedAnime.add(name);
            }
            else if(planToWatch)
            {
                if(name.equals("Dropped"))
                {
                    dropped = true;
                    continue;
                }
                planToWatchAnime.add(name);
            }
            else if(watching)
            {
                if(name.equals("Plan to watch"))
                {
                    planToWatch = true;
                    continue;
                }
                watchingAnime.add(name);
            }
            else if (onHold) 
            {
                if(name.equals("Watching"))
                {
                    watching = true;
                    continue;
                }
                onHoldAnime.add(name);
                
            }
            // checks if the name is "On-Hold"
            else if (name.equals("On-Hold")) 
            {
                // if it is, sets the flag to true
                onHold = true;
            } 
            else 
            {
                if(names.get(0).equals(name))
                continue;
                // adds the name to the completed list
                completedAnime.add(name);
            }
        }
        Scanner input = new Scanner(System.in);

        System.out.println("\nPlease be aware that the converted MyAnimeList.xml file does not include information on the\n" 
        + "number of watched episodes. This may affect anime in the \"watching\" \"on-hold\" and \"dropped\" categories\n"
        + "and may overwrite existing entries on MyAnimeList with the default value of 0 episodes watched.\n\n"
        + "To avoid this would you like to enter in the amount of episodes watched for these anime? (Yes = 1 / No = -1)");
        int x = input.nextInt();

        System.out.println("");

        // create an empty list to store the watched episode numbers
        List<Integer> watchedEpisodes = new ArrayList<>();
    
        if(x != -1)
        {
            // add 0 for the length of the completedAnime list
            for (int i = 0; i < completedAnime.size(); i++) 
            {
                watchedEpisodes.add(0);
            }

            // prompt the user to enter the watched episode numbers for onHoldAnime and watchingAnime
            for (int i = 0; i < onHoldAnime.size(); i++) 
            {
                System.out.println("Enter the watched episode number for " + onHoldAnime.get(i) + ": ");
                int watched = input.nextInt();
                watchedEpisodes.add(watched);
            }
            for (int i = 0; i < watchingAnime.size(); i++) 
            {
                System.out.println("Enter the watched episode number for " + watchingAnime.get(i) + ": ");
                int watched = input.nextInt();
                watchedEpisodes.add(watched);
            }

            // add 0 for the length of the planToWatchAnime list
            for (int i = 0; i < planToWatchAnime.size(); i++) 
            {
                watchedEpisodes.add(0);
            }

            // add the watched episode numbers for the droppedAnime list
            for (int i = 0; i < droppedAnime.size(); i++) 
            {
                System.out.println("Enter the watched episode number for " + droppedAnime.get(i) + ": ");
                int watched = input.nextInt();
                watchedEpisodes.add(watched);
            }
            
        }
        else
        {
            int zero = 0;
            for(int i = 0; i < names.size(); i++)
            {
                watchedEpisodes.add(zero);
            }
        }
        input.close();
        
        prtList(completedAnime, onHoldAnime, watchingAnime, planToWatchAnime, droppedAnime);

        generateXML(completedAnime, onHoldAnime, watchingAnime, planToWatchAnime, droppedAnime, animedbID, watchedEpisodes);
    }

    public static void generateXML(List<String> completedAnime, List<String> onHoldAnime, 
    List<String> watchingAnime, List<String> planToWatchAnime, List<String> droppedAnime, 
    List<String> animedbID, List<Integer> watchedEpisodes)throws Exception
    {
        int listsize1 = completedAnime.size();
        int listsize2 = listsize1 + onHoldAnime.size();
        int listsize3 = listsize2 + watchingAnime.size();
        int listsize4 = listsize3 + planToWatchAnime.size();
        int totalsize = listsize4 + droppedAnime.size();

        // create a new XML document
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();

        // create the root element "myanimelist"
        Element root = doc.createElement("myanimelist");
        doc.appendChild(root);

        Element myinfo = doc.createElement("myinfo");
        root.appendChild(myinfo);

        Element userExportType = doc.createElement("user_export_type");
        myinfo.appendChild(userExportType);
        Text userExportTypeText = doc.createTextNode("1");
        userExportType.appendChild(userExportTypeText);

        // create the "anime" elements and their sub elements
        for (int i = 0; i < totalsize; i++) {
            Element anime = doc.createElement("anime");
            root.appendChild(anime);

            Element seriesAnimedbId = doc.createElement("series_animedb_id");
            Text seriesAnimedbIdText = doc.createTextNode(animedbID.get(i));
            seriesAnimedbId.appendChild(seriesAnimedbIdText);
            anime.appendChild(seriesAnimedbId);

            Element seriesTitle = doc.createElement("series_title");
            if(listsize1 > i)
            {
                CDATASection seriesTitleCDATA = doc.createCDATASection(completedAnime.get(i));
                seriesTitle.appendChild(seriesTitleCDATA);
            }
            else if(listsize2 > i)
            {
                CDATASection seriesTitleCDATA = doc.createCDATASection(onHoldAnime.get(i-listsize1));
                seriesTitle.appendChild(seriesTitleCDATA);
            }
            else if(listsize3 > i)
            {
                CDATASection seriesTitleCDATA = doc.createCDATASection(watchingAnime.get(i-listsize2));
                seriesTitle.appendChild(seriesTitleCDATA);
            }
            else if(listsize4 > i)
            {
                CDATASection seriesTitleCDATA = doc.createCDATASection(planToWatchAnime.get(i-listsize3));
                seriesTitle.appendChild(seriesTitleCDATA);
            }
            else if(totalsize > i)
            {
                CDATASection seriesTitleCDATA = doc.createCDATASection(droppedAnime.get(i-listsize4));
                seriesTitle.appendChild(seriesTitleCDATA);
            }
            anime.appendChild(seriesTitle);

         
            Element myWatchedEpisodes = doc.createElement("my_watched_episodes");
            Text myWatchedEpisodesText = doc.createTextNode(String.valueOf(watchedEpisodes.get(i)));
            myWatchedEpisodes.appendChild(myWatchedEpisodesText);
            anime.appendChild(myWatchedEpisodes);
     

            Element myStatus = doc.createElement("my_status");
            if(listsize1 > i)
            {
                Text myStatusText = doc.createTextNode("Completed");
                myStatus.appendChild(myStatusText);
            }
            else if(listsize2 > i)
            {
                Text myStatusText = doc.createTextNode("On-Hold");
                myStatus.appendChild(myStatusText);
            }
            else if(listsize3 > i)
            {
                Text myStatusText = doc.createTextNode("Watching");
                myStatus.appendChild(myStatusText);
            }
            else if(listsize4 > i)
            {
                Text myStatusText = doc.createTextNode("Plan to Watch");
                myStatus.appendChild(myStatusText);
            }
            else if(totalsize > i)
            {
                Text myStatusText = doc.createTextNode("Dropped");
                myStatus.appendChild(myStatusText);
            }
            anime.appendChild(myStatus);

            Element updateOnImport = doc.createElement("update_on_import");
            Text updateOnImportText = doc.createTextNode("1");
            updateOnImport.appendChild(updateOnImportText);
            anime.appendChild(updateOnImport);
        }

        // create a Transformer object and transform the XML document into a stream of bytes
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult("MyAnimeList.xml");
        transformer.transform(source, result);
    }
    public static void prtList(List<String> completedAnime, List<String> onHoldAnime, 
    List<String> watchingAnime, List<String> planToWatchAnime, List<String> droppedAnime)
    {
        // prints the completed list
        System.out.println("\n\n---------------------------- Completed Anime ---------------------------- \n");
        System.out.println(completedAnime);
        System.out.println("\n\n----------------------------- On-Hold Anime ----------------------------- \n");
        System.out.println(onHoldAnime);
        System.out.println("\n\n---------------------------- Watching Anime ----------------------------- \n");
        System.out.println(watchingAnime);
        System.out.println("\n\n-------------------------- Plan to Watch Anime -------------------------- \n");
        System.out.println(planToWatchAnime);
        System.out.println("\n\n----------------------------- Dropped Anime ----------------------------- \n");
        System.out.println(droppedAnime);
        System.out.println("\n\n");
    }
}

