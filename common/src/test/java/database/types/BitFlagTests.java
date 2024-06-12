package database.types;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.github.chitchat.common.storage.database.types.BitFlag;
import java.util.EnumSet;
import org.junit.jupiter.api.Test;

public class BitFlagTests {

    @Test
    public void intoBitMask() {
        var set = EnumSet.of(TestEnum.FIRST, TestEnum.SECOND);
        var manualMask = (1 << TestEnum.FIRST.ordinal()) | (1 << TestEnum.SECOND.ordinal());
        var mask = BitFlag.toBitMask(set);

        assertEquals(mask, manualMask);
    }

    @Test
    public void fromBitMask() {
        var manualSet = EnumSet.of(TestEnum.FIRST, TestEnum.SECOND);
        var mask = (1 << TestEnum.FIRST.ordinal()) | (1 << TestEnum.SECOND.ordinal());
        var set = BitFlag.fromBitMask(TestEnum.class, mask);

        assertEquals(set, manualSet);
    }

    @Test
    public void setBitMask() {
        var set = EnumSet.noneOf(TestEnum.class);
        var manualSet = EnumSet.of(TestEnum.FIRST, TestEnum.SECOND);
        var mask = 1 << (TestEnum.FIRST.ordinal()) | (1 << TestEnum.SECOND.ordinal());
        BitFlag.setBitMask(set, mask);

        assertEquals(set, manualSet);
    }
}
