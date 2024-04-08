package business.Impl;

import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.Message;
import com.google.protobuf.Timestamp;
import com.google.protobuf.TimestampOrBuilder;
import lombok.Data;
import lombok.Setter;
import ru.tinkoff.piapi.contract.v1.HistoricCandleOrBuilder;
import ru.tinkoff.piapi.contract.v1.Quotation;
import ru.tinkoff.piapi.contract.v1.QuotationOrBuilder;

@Setter
public class HistoricCandleFake extends GeneratedMessageV3 implements HistoricCandleOrBuilder {
    private static final long serialVersionUID = 0L;
    public static final int OPEN_FIELD_NUMBER = 1;
    private Quotation open_;
    public static final int HIGH_FIELD_NUMBER = 2;
    private Quotation high_;
    public static final int LOW_FIELD_NUMBER = 3;
    private Quotation low_;
    public static final int CLOSE_FIELD_NUMBER = 4;
    private Quotation close_;
    public static final int VOLUME_FIELD_NUMBER = 5;
    private long volume_;
    public static final int TIME_FIELD_NUMBER = 6;
    private Timestamp time_;
    public static final int IS_COMPLETE_FIELD_NUMBER = 7;
    private boolean isComplete_;
    private byte memoizedIsInitialized;

    @Override
    protected FieldAccessorTable internalGetFieldAccessorTable() {
        return null;
    }

    @Override
    protected Message.Builder newBuilderForType(BuilderParent builderParent) {
        return null;
    }

    @Override
    public Message.Builder newBuilderForType() {
        return null;
    }

    @Override
    public Message.Builder toBuilder() {
        return null;
    }

    @Override
    public boolean hasOpen() {
        return false;
    }

    @Override
    public Quotation getOpen() {
        return open_;
    }

    @Override
    public QuotationOrBuilder getOpenOrBuilder() {
        return null;
    }

    @Override
    public boolean hasHigh() {
        return false;
    }

    @Override
    public Quotation getHigh() {
        return high_;
    }

    @Override
    public QuotationOrBuilder getHighOrBuilder() {
        return null;
    }

    @Override
    public boolean hasLow() {
        return false;
    }

    @Override
    public Quotation getLow() {
        return low_;
    }

    @Override
    public QuotationOrBuilder getLowOrBuilder() {
        return null;
    }

    @Override
    public boolean hasClose() {
        return false;
    }

    @Override
    public Quotation getClose() {
        return close_;
    }

    @Override
    public QuotationOrBuilder getCloseOrBuilder() {
        return null;
    }

    @Override
    public long getVolume() {
        return volume_;
    }

    @Override
    public boolean hasTime() {
        return false;
    }

    @Override
    public Timestamp getTime() {
        return time_;
    }

    @Override
    public TimestampOrBuilder getTimeOrBuilder() {
        return null;
    }

    @Override
    public boolean getIsComplete() {
        return false;
    }

    @Override
    public Message getDefaultInstanceForType() {
        return null;
    }
}
