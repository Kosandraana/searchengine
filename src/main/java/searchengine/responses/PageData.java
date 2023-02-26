package searchengine.responses;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
    public class PageData {
    private String site;
    private String siteName;
    private String uri;
    private String title;
    private String snippet;
    private float relevance;

//    public PageData(String site, String siteName, String uri, String title, String snippet, float relevance) {
//        this.site = site;
//        this.siteName = siteName;
//        this.uri = uri;
//        this.title = title;
//        this.snippet = snippet;
//        this.relevance = relevance;
//    }
//    public PageData() {}
//
//    public String toString() {
//        return "PageData{" +
//                "uri='" + uri + '\'' +
//                ", title='" + title + '\'' +
//                ", snippet='" + snippet + '\'' +
//                ", relevance=" + relevance +
//                '}';
//    }
}
