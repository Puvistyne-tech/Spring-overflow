package uge.fr.ugeoverflow.filters;

public enum QuestionFilterType {

    NEWEST("Newest"),
    OLDEST("Oldest"),
    UNANSWERED("Unanswered"),
    ANSWERED("Answered");

    private final String filterName;

    QuestionFilterType(String filterName) {
        this.filterName = filterName;
    }

    public String getFilterName() {
        return filterName;
    }
}
