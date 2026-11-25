public class Word {

    private final int id;
    private final String word;
    private final PartOfSpeech partOfSpeech;
    private final int syllables;


    public Word(int id, String word, PartOfSpeech partOfSpeech, int syllables) {
        this.id = id;
        this.word = word;
        this.partOfSpeech = partOfSpeech;
        this.syllables = syllables;
    }

    public int getId() {
        return id;
    }

    public String getWord() {
        return word;
    }

    public PartOfSpeech getPartOfSpeech() {
        return partOfSpeech;
    }

    public int getSyllables() {
        return syllables;
    }
}
