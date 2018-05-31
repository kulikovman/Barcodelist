package ru.kulikovman.barcodelist;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.Sort;
import ru.kulikovman.barcodelist.model.Good;

public class RealmHelper {
    private static RealmHelper sRealmHelper;
    private Realm mRealm;

    public static RealmHelper get() {
        if (sRealmHelper == null) {
            sRealmHelper = new RealmHelper();
        }
        return sRealmHelper;
    }

    private RealmHelper() {
        mRealm = Realm.getDefaultInstance();
    }

    Good getGoodById(long goodId) {
        return mRealm.where(Good.class)
                .equalTo(Good.ID, goodId)
                .findFirst();
    }

    OrderedRealmCollection<Good> getAllGoods() {
        return mRealm.where(Good.class)
                .findAll()
                .sort(new String[]{Good.GROUP, Good.NAME},
                        new Sort[]{Sort.ASCENDING, Sort.DESCENDING});
    }
}
