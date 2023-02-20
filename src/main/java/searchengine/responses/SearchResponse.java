package searchengine.responses;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
    @Setter
    @Data
    @EqualsAndHashCode(callSuper = false)
    public class SearchResponse extends Response {
    private int count;
    private boolean result;
//    private List<PageData> data = new ArrayList<>();
        private PageData[] data;


    public SearchResponse(boolean result) {
        this.result = result;
    }
    public SearchResponse(boolean result, int count, PageData[] data) {
        this.count = count;
        this.result = result;
        this.data = data;
    }
}