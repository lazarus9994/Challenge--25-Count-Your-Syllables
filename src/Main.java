import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Main {
    public static void main(String[] args){

        List<Word> words = new ArrayList<>();

        try{
            String jsonData = new String(Files.readAllBytes(Paths.get("words.json")), StandardCharsets.UTF_8);


            JSONArray wordArray = new JSONArray(jsonData);

            for (int i = 0; i < wordArray.length(); i++) {
                JSONObject wordObj = wordArray.getJSONObject(i);
                Word word = new Word(wordObj.getInt("id"), wordObj.getString("word"), PartOfSpeech.valueOf(wordObj.get("part_of_speech").toString().toUpperCase()), wordObj.getInt("syllables"));
                words.add(word);

            }

        }catch (IOException e){
            e.printStackTrace();
        }


        List<Word> currentPoem = new ArrayList<>();

        Map<PartOfSpeech, List<Word>> sentenceComponents = new EnumMap<>(PartOfSpeech.class);


        for (Word word : words){
            if (word.getPartOfSpeech().equals(PartOfSpeech.ADJECTIVE) || word.getPartOfSpeech().equals(PartOfSpeech.ADVERB) ||
                    word.getPartOfSpeech().equals(PartOfSpeech.NOUN) || word.getPartOfSpeech().equals(PartOfSpeech.VERB)){

                sentenceComponents.putIfAbsent(word.getPartOfSpeech(), new ArrayList<>());
                sentenceComponents.get(word.getPartOfSpeech()).add(word);

            }
        }

    }


}
