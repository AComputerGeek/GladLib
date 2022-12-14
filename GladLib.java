import edu.duke.*;
import java.util.*;

/**
* 
* @author: Amir Armion 
* 
* @version: V.01
* 
*/
public class GladLib
{
    private ArrayList<String> adjectiveList;
    private ArrayList<String> nounList;
    private ArrayList<String> colorList;
    private ArrayList<String> countryList;
    private ArrayList<String> nameList;
    private ArrayList<String> animalList;
    private ArrayList<String> timeList;
    private ArrayList<String> verbList;
    private ArrayList<String> fruitList;

    private ArrayList<String> wordsUsed;

    private Random myRandom;

    private static String dataSourceURL       = "http://dukelearntoprogram.com/course3/data";
    private static String dataSourceDirectory = "data";

    // Constructor
    public GladLib()
    {
        initializeFromSource(dataSourceDirectory);
        wordsUsed = new ArrayList<>();
        myRandom  = new Random();
    }

    public GladLib(String source)
    {
        initializeFromSource(source);
        wordsUsed = new ArrayList<>();
        myRandom  = new Random();
    }

    private void initializeFromSource(String source) 
    {
        adjectiveList = readIt(source + "/adjective.txt");  
        nounList      = readIt(source + "/noun.txt");
        colorList     = readIt(source + "/color.txt");
        countryList   = readIt(source + "/country.txt");
        nameList      = readIt(source + "/name.txt");       
        animalList    = readIt(source + "/animal.txt");
        timeList      = readIt(source + "/timeframe.txt");  
        verbList      = readIt(source + "/verb.txt");
        fruitList     = readIt(source + "/fruit.txt");
    }

    private ArrayList<String> readIt(String source)
    {
        ArrayList<String> list = new ArrayList<String>();

        if(source.startsWith("http")) 
        {
            URLResource resource = new URLResource(source);

            for(String line: resource.lines())
            {
                list.add(line);
            }
        }
        else 
        {
            FileResource resource = new FileResource(source);

            for(String line: resource.lines())
            {
                list.add(line);
            }
        }

        return list;
    }

    private String getSubstitute(String label) 
    {        
        if(label.equals("country")) 
        {
            return randomFrom(countryList);
        }

        if(label.equals("color"))
        {
            return randomFrom(colorList);
        }

        if(label.equals("noun"))
        {
            return randomFrom(nounList);
        }

        if(label.equals("name"))
        {
            return randomFrom(nameList);
        }

        if(label.equals("adjective"))
        {
            return randomFrom(adjectiveList);
        }

        if(label.equals("animal"))
        {
            return randomFrom(animalList);
        }

        if(label.equals("timeframe"))
        {
            return randomFrom(timeList);
        }

        if(label.equals("verb"))
        {
            return randomFrom(verbList);
        }

        if(label.equals("fruit"))
        {
            return randomFrom(fruitList);
        }

        if(label.equals("number"))
        {
            return "" + myRandom.nextInt(50) + 5;
        }

        return "** UNKNOWN **";
    }

    private String randomFrom(ArrayList<String> source)
    {
        int index = myRandom.nextInt(source.size());

        return source.get(index);
    }

    private String processWord(String w)
    {
        int first = w.indexOf("<");
        int last  = w.indexOf(">", first);

        if(first == -1 || last == -1)
        {
            return w;
        }

        String prefix = w.substring(0, first);
        String suffix = w.substring(last + 1);
        String lable  = w.substring(first + 1, last);

        String sub    = getSubstitute(lable);

        while(wordsUsed.contains(sub))
        {
            sub = getSubstitute(lable);
        }

        wordsUsed.add(sub);

        return prefix + sub + suffix;
    }

    private String fromTemplate(String source)
    {
        String story = "";

        if(source.startsWith("http")) 
        {
            URLResource resource = new URLResource(source);

            for(String word: resource.words())
            {
                story = story + processWord(word) + " ";
            }
        }
        else 
        {
            FileResource resource = new FileResource(source);

            for(String word: resource.words())
            {
                story = story + processWord(word) + " ";
            }
        }

        return story;
    }

    private void printOut(String s, int lineWidth)
    {
        int charsWritten = 0;

        for(String w: s.split("\\s+"))
        {
            if(charsWritten + w.length() > lineWidth)
            {
                System.out.println();
                charsWritten = 0;
            }

            System.out.print(w + " ");
            charsWritten += w.length() + 1;
        }
    }

    public void makeStory()
    {
        wordsUsed.clear();

        String story = fromTemplate("data/madtemplate3.txt");

        printOut(story, 60);

        System.out.println("\n\n- The total number of words that were replaced: " + wordsUsed.size() + " words\n");
    }
}
