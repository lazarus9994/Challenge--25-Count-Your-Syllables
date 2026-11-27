import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {

        List<Word> words = new ArrayList<>();

        try {
            String jsonData = new String(Files.readAllBytes(Paths.get("words.json")), StandardCharsets.UTF_8);


            JSONArray wordArray = new JSONArray(jsonData);

            for (int i = 0; i < wordArray.length(); i++) {
                JSONObject wordObj = wordArray.getJSONObject(i);
                Word word = new Word(wordObj.getInt("id"), wordObj.getString("word"), PartOfSpeech.valueOf(wordObj.get("part_of_speech").toString().toUpperCase()), wordObj.getInt("syllables"));
                words.add(word);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        List<Word> currentPoem = new ArrayList<>();

        Random random = new Random();

        Map<PartOfSpeech, List<Word>> sentenceComponents = new EnumMap<>(PartOfSpeech.class);


        for (Word word : words) {
            if (word.getPartOfSpeech().equals(PartOfSpeech.ADJECTIVE) || word.getPartOfSpeech().equals(PartOfSpeech.ADVERB) ||
                    word.getPartOfSpeech().equals(PartOfSpeech.NOUN) || word.getPartOfSpeech().equals(PartOfSpeech.VERB)) {

                sentenceComponents.putIfAbsent(word.getPartOfSpeech(), new ArrayList<>());
                sentenceComponents.get(word.getPartOfSpeech()).add(word);

            }
        }

        List<Word> poem = new ArrayList<>();

        Map<PartOfSpeech, Boolean> coveredTypes = new EnumMap<>(PartOfSpeech.class);
        coveredTypes.put(PartOfSpeech.ADJECTIVE, false);
        coveredTypes.put(PartOfSpeech.ADVERB, false);
        coveredTypes.put(PartOfSpeech.NOUN, false);
        coveredTypes.put(PartOfSpeech.VERB, false);

        for (int i = 0; i < 3; i++) {
            int count = 0;


            if (i % 2 == 0) {
                count = 5;
            } else count = 7;

            List<Word> currentLine = new ArrayList<>();

            if (i == 0) {
                List<Word> starterWords = words.stream()
                        .filter(word -> word.getSyllables() <= 5)
                        .toList();

                Word starter = starterWords.get(random.nextInt(starterWords.size()));

                currentLine.add(starter);
                if (sentenceComponents.containsKey(starter.getPartOfSpeech())) {
                    coveredTypes.replace(starter.getPartOfSpeech(), true);
                }
            }


            while (currentLine.stream().mapToInt(Word::getSyllables).sum() < count) {

                Word adjectiveWord = sentenceComponents.get(PartOfSpeech.ADJECTIVE).get(random.nextInt(sentenceComponents.size()));
                Word adverbWord = sentenceComponents.get(PartOfSpeech.ADVERB).get(random.nextInt(sentenceComponents.size()));
                Word nounWord = sentenceComponents.get(PartOfSpeech.NOUN).get(random.nextInt(sentenceComponents.size()));
                Word verbWord = sentenceComponents.get(PartOfSpeech.VERB).get(random.nextInt(sentenceComponents.size()));

                if (currentLine.stream().mapToInt(w -> w.getSyllables()).sum() + adjectiveWord.getSyllables() <= count && !currentLine.contains(adjectiveWord)) {
                    currentLine.add(adjectiveWord);
                } else if (currentLine.stream().mapToInt(w -> w.getSyllables()).sum() + adverbWord.getSyllables() <= count && !currentLine.contains(adverbWord)) {
                    currentLine.add(adverbWord);
                } else if (currentLine.stream().mapToInt(w -> w.getSyllables()).sum() + nounWord.getSyllables() <= count && !currentLine.contains(nounWord)) {
                    currentLine.add(nounWord);
                } else if (currentLine.stream().mapToInt(w -> w.getSyllables()).sum() + verbWord.getSyllables() <= count && !currentLine.contains(verbWord)) {
                    currentLine.add(verbWord);
                }

                for (Word word : words) {
                    if (currentLine.stream().mapToInt(w -> w.getSyllables()).sum() + word.getSyllables() <= count && !currentLine.contains(word)) {
                        currentLine.add(word);
                    }

                    if (currentLine.stream().mapToInt(w -> w.getSyllables()).sum() == count) {
                        break;
                    }
                }

//                for (Map.Entry<PartOfSpeech, List<Word>> entry : sentenceComponents.entrySet()){
//                    if (!coveredTypes.get(entry.getKey())){
//                        List<Word> entryList = entry.getValue().stream().toList();
//                        Word randomWord = entryList.get(random.nextInt(entryList.size()));
//                        if (currentLine.stream().mapToInt(Word::getSyllables).sum() + randomWord.getSyllables() <= count && !poem.contains(randomWord)  && !currentLine.contains(randomWord)){
//                            currentLine.add(randomWord);
//                            coveredTypes.replace(randomWord.getPartOfSpeech(), true);
//                        }
//                    } else {
//                        Word randomWord = words.get(random.nextInt(words.size()));
//                        if (currentLine.stream().mapToInt(Word::getSyllables).sum() + randomWord.getSyllables() <= count && !poem.contains(randomWord) && !currentLine.contains(randomWord)){
//                            currentLine.add(randomWord);
//
//                            if (coveredTypes.containsKey(randomWord.getPartOfSpeech())){
//                                coveredTypes.replace(randomWord.getPartOfSpeech(), true);
//                            }
//
//                        }
//                    }
//                }
//            }
                poem.addAll(currentLine);

                currentLine.clear();


            }
        }
    }
}
