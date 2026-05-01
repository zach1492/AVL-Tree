/**
 * Controls a record and has data that contains genre, year, artist and title varibles
 * 
 * Has a compares to method that compares records by (in order of importance) genre, year, artist and title 
 */
public class Record implements Comparable<Record> {

    private String genre;
    private int year;
    private String artist;
    private String title;

    /**
     * Record constructor
     * 
     * @param genre is the varible that hold the records genre
     * @param year holds the year the songs was released
     * @param artist holds the songs artist
     * @param title is the what the songs called
     */
    public Record(String genre, int year, String artist, String title){
        this.genre = genre;
        this.year = year;
        this.artist = artist;
        this.title = title;
    }
    
    /**
     * returns the songs genre
     * 
     * @return genre string
     */
    public String getGenre(){
        return genre;
    }

    /**
     * returns the songs year
     * 
     * @return year int
     */
    public int getYear(){
        return year;
    }

    /**
     * returns the songs artist
     * 
     * @return artist string
     */
    public String getArtist(){
        return artist;
    }

    /**
     * returns the songs title
     * 
     * @return title string
     */
    public String getTitle(){
        return title;
    }

    /**
     * returns the songs infomation in a string
     * 
     * @return formatted string of all the data
     */
    @Override
    public String toString(){
        return genre + " | " + year + " | " + artist + " | " + title;
    }

    /**
     * Compares this record to anthour
     * firstly by genre
     * secondly by year
     * thirdly by artist
     * and lastly by title
     * 
     * All strings are compared lexographicly
     * and all ints are compared by size
     * 
     * @param other is the record this record is getting compared to
     * @return a negative number if this record comes before other, a 0 if its equal to other and a positive number is its after other
     */
    @Override
    public int compareTo(Record other) {
        int comparisonNum = this.genre.compareTo(other.genre);

        if(comparisonNum != 0)
            return comparisonNum;

        comparisonNum = Integer.compare(this.year, other.year);

        if(comparisonNum != 0)
            return comparisonNum;

        comparisonNum = this.artist.compareTo(other.artist);
        if(comparisonNum != 0)
            return comparisonNum;

        return this.title.compareTo(other.title);
    }
}
