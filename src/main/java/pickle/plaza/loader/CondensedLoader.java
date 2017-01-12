package pickle.plaza.loader;

import pickle.plaza.analyze.WordList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Loader for 'condensed' word lists. The file in the repo is a sentiwordnet file with redundant information stripped out
 * to bring down the size by quite a large margin.
 */
public class CondensedLoader implements WordListLoader
{
    @Override
    public void load(WordList list, InputStream stream) throws IOException
    {
        try (BufferedReader ins = new BufferedReader(new InputStreamReader(stream)))
        {
            String line;
            while ((line = ins.readLine()) != null)
            {
                addLine(list, line);
            }
        }
    }

    private void addLine(WordList list, String line)
    {
        String[] parts = line.split(",");
        list.add(parts[0], Double.parseDouble(parts[1]), Double.parseDouble(parts[2]));
    }
}
