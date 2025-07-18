package de.jcup.kubegen;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimpleTemplateGenerator {

    private Map<String, Pattern> patternCache = new HashMap<>();
    private boolean debugOutput;
    
    void enableDebugOutput() {
        this.debugOutput=true;
    }

    public void generate(GenerationContext c) throws IOException {
        File ouputFile = new File(c.targetFolder, c.templateFile.getName());
        try (BufferedReader br = new BufferedReader(new FileReader(c.templateFile));
                FileWriter fw = new FileWriter(ouputFile)) {
            String line = null;
            StringBuilder sb = new StringBuilder();
            c.lineNr = 0;
            while ((line = br.readLine()) != null) {
                boolean firstLine = c.lineNr == 0;
                if (!firstLine) {
                    sb.append("\n");
                }
                c.lineNr++;
                String newLine = replacePlaceHolders(line, c);
                sb.append(newLine);
            }
            fw.write(sb.toString());

        }

    }

    String replacePlaceHolders(String line, GenerationContext c) {
        Set<String> keys = c.project.getCommonAndEnvKeys(c.environment);

        for (String key : keys) {

            if (line.indexOf(key) != -1) {
                /* we use a pattern cache to reduce double compiles */
                Pattern pattern = patternCache.get(key);
                if (pattern == null) {
                    /* create pattern */
                    String quotedKey = Pattern.quote(key);
                    String regex = "\\{\\{\\s*\\." + quotedKey + "\\s*\\}\\}";
                    pattern = Pattern.compile(regex);

                    patternCache.put(key, pattern);
                }
                String value = c.project.getValue(c.environment, key);
                String quotedReplacement = Matcher.quoteReplacement(value);
                try {
                    line = pattern.matcher(line).replaceAll(quotedReplacement);
                } catch (IllegalArgumentException e) {
                    if(!debugOutput) {
                        throw e;
                    }
                    /* when debug output is enabled, we throw more information - why only for debugging ? because values could be sensitive in usage and we do not want this always to be in logs etc.*/ 
                    throw new IllegalArgumentException(
                            "Was not able to replace\n line : " + line + "\nfor key :'" + key + "'\norigin value '"+value +"'\nwith quoted value '" + quotedReplacement+"'"+"\nPattern="+pattern, e);
                }

            }
        }
        int index = line.indexOf("{{");
        int lastIndex = line.indexOf("}}");
        boolean foundKeyNotResolved = index != -1 && lastIndex != -1;
        if (foundKeyNotResolved) {
            throw new MissingKeyException("Line:" + c.lineNr + "=" + line.substring(index, lastIndex), c.templateFile);
        }
        return line;
    }

}
