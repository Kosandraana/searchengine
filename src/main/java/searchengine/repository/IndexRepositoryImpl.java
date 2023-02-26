package searchengine.repository;

import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import searchengine.config.Props;
import searchengine.model.Indexx;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class IndexRepositoryImpl implements IndexRepositoryCustom {
    private static Logger log;
    @Autowired
    EntityManager entityManager;
    public static final String TABS = "\t\t";

    @Override
    public void insertIndexList(String siteName, List<Indexx> indexes) {
        int ONE_THOUSAND = 1000;
        int SAVING_PORTION = 100 * ONE_THOUSAND;
        StringBuilder insertBuilder = new StringBuilder();
        int currIndex = 0;
        while (currIndex < indexes.size()) {
            currIndex = buildInserts(indexes, insertBuilder, currIndex);

            String sql = Props.getInst().getMultiInsertString() +
                    " values " + insertBuilder;
            insertBuilder.setLength(0);
            Query query = entityManager.
                    createNativeQuery(sql);
            query.executeUpdate();
            if (currIndex % SAVING_PORTION == 0) {
                log.info(TABS + "Сайт \"" + siteName + "\": " +
                        "сохранено " + currIndex / ONE_THOUSAND + " тыс. индексов");
            } else {
                log.info(TABS + "Сайт \"" + siteName + "\": " +
                        "сохранено " + currIndex + " индексов");
            }
        }
    }

    private int buildInserts(List<Indexx> indexes, StringBuilder insertBuilder, int currIndex) {
        for (int i = 0; i++ < 100_000 && currIndex < indexes.size(); currIndex++) {
            Indexx index = indexes.get(currIndex);
            insertBuilder.append(insertBuilder.length() == 0 ? "" : ",")
                    .append("(").append(index.getPage().getId())
                    .append(",").append(index.getLemma().getId())
                    .append(",").append(index.getRank()).append(")");
        }
        return currIndex;
    }
}

