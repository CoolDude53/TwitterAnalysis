package pickle.plaza.loader;

import pickle.plaza.analyze.WordList;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public interface WordListLoader
{
    default void load(WordList list, File file) throws IOException
    {
        load(list, new FileInputStream(file));
    }

    void load(WordList list, InputStream stream) throws IOException;
}
