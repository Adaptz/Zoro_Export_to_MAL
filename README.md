# Zoro to MyAnimeList XML Converter

This program converts the "export.xml" file from the Zoro.to anime website into a format that can be imported into MyAnimeList.

## **Disclaimer:**

- The converted MyAnimeList.xml does not have the info for the amount of watched episodes, so by default it will be set to 0.
  - This only matters for anime in the watching, on-hold, and dropped categories since the completed and plan to watch categories
    are still valid despite being set at 0.
  - Please be aware if you already have any anime on the Zoro export on your MyAnimeList
    they will be automatically set to 0 after importing if you skip the step of entering them in manually.

## How to use (Replit)

(This is the easiest option for those that don't have experience running java code)

1. Export your anime list from Zoro.to by going to <https://zoro.to/user/mal?tab=export>, make sure "Export format" is set to XML and "Group by Folder" is on.
2. Click the "Export" button to export your list.
3. Change the XML so that the order of the folders is completed, onHold, watching, planToWatch, dropped (cut and paste from \<folder> to \</folder>).
4. Create a replit account or log in <https://replit.com/signup>.
5. Choose the "Create" button
6. Under Template type in "Java" and choose the first option.
7. Create a title for the repl and choose "Create Repl."
8. Open the "zoroToMalConvert.java" in a text file and copy and paste it in to the Main.java file inside the new Repl.
9. Create a new file using the New File button to the direct right of the Files dropdown in Repl and name it "export.xml" (this is case-sensitive).
10. Open the "export.xml" file that you got from Zoro in a text file and copy and paste it to the new file you just created.
11. Select the Run button on the top of the page.
12. Read the message in the Console and enter the watched episodes for the 3 categories mentioned in the disclaimer (unless you don't mind them being set to 0).
13. The program will generate a MyAnimeList.xml file under the Files dropdown, this is the file that will be able to be used on MyAnimeList.
14. Download the MyAnimeList.xml file using the three dots that appear when hovering over the file name and select download.
15. On MyAnimeList, go to <https://myanimelist.net/import.php>.
16. Find the "Choose import type" dropdown and select "MyAnimeList Import".
17. Click the "Import" button to import your list.

## How to use (Java Compiler)

1. Export your anime list from Zoro.to by going to <https://zoro.to/user/mal?tab=export>, make sure "Export format" is set to XML and "Group by Folder" is on.
2. Click the "Export" button to export your list.
3. Change the XML so that the order of the folders is completed, onHold, watching, planToWatch, dropped.
4. Save the resulting export.xml file to the same directory as the Java program.
5. Using you're compiler of choice open the folder run the Java program.
6. Read the message in the terminal and enter the watched episodes for the 3 categories mentioned in the disclaimer (unless you don't mind them being set to 0).
7. The program will generate a MyAnimeList.xml file in the same directory, this is the file that will be able to be used on MyAnimeList.
8. On MyAnimeList, go to <https://myanimelist.net/import.php>.
9. Find the "Choose import type" dropdown and select "MyAnimeList Import".
10. Click the "Import" button to import your list.
