package pickle.plaza.loader;

import pickle.plaza.TwitterMain;
import pickle.plaza.analyze.WordList;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.Properties;

public class Loader
{
    public static WordList loadWordList(Properties config) throws IOException
    {
        final WordList list = new WordList();
        final WordListLoader loader;

        switch (config.getProperty("wordlist.type"))
        {
            case "condensed":
                loader = new CondensedLoader();
                break;
            case "sentinet":
                loader = new SentiWordNetLoader();
                break;
            default:
                loader = getLoader(config.getProperty("wordlist.type"));
                break;
        }

        TwitterMain.LOG.info("WordListLoader type {}", loader.getClass().getName());

        String file = config.getProperty("wordlist.file");

        if (file.startsWith("jar:"))
        {
            file = file.replace("jar:", "");
            TwitterMain.LOG.info("Loading wordlist from jar: {}", file);
            InputStream ins = TwitterMain.class.getResourceAsStream(file);
            if (ins == null)
            {
                throw new RuntimeException("File " + file + " not found in jar.");
            }
            loader.load(list, ins);
        }
        else
        {
            TwitterMain.LOG.info("Loading wordlist from file: {}", file);
            loader.load(list, new File(file));
        }

        TwitterMain.LOG.info("Loaded wordlist with {} entries.", list.size());

        return list;
    }

    private static WordListLoader getLoader(String className)
    {
        try
        {
            Constructor<?> con = Class.forName(className).getConstructor();
            Object instance = con.newInstance();

            if (!(instance instanceof WordListLoader))
            {
                throw new ClassNotFoundException("Class " + className + " does not implement " + WordListLoader.class.getName());
            }

            return (WordListLoader) instance;
        } catch (ClassNotFoundException e)
        {
            throw new RuntimeException("Class " + className + " not found.");
        } catch (Exception e)
        {
            throw new RuntimeException("Class " + className + " does not have a public parameterless constructor.");
        }
    }
}
