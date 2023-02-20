package searchengine.responses;

import lombok.Data;
import lombok.EqualsAndHashCode;

    @Data
    @EqualsAndHashCode(callSuper = false)
    public class ErrorResponse extends Response {
    private String error;
//    private boolean result;

    public ErrorResponse(String error) {
        this.error = error;
    }
        public boolean getResult() {
            return false;
        }
        public String getError() {
            return error;
        }
}
