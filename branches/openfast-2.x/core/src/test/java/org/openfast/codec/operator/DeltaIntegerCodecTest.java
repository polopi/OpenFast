package org.openfast.codec.operator;

import static org.openfast.codec.operator.FastOperatorTestHarness.INITIAL_VALUE;
import static org.openfast.codec.operator.FastOperatorTestHarness.NO_INITIAL_VALUE;
import static org.openfast.codec.operator.FastOperatorTestHarness.UNDEFINED;
import org.openfast.Fast;
import org.openfast.template.Scalar;
import org.openfast.template.operator.DeltaOperator;
import org.openfast.template.type.Type;
import org.openfast.test.OpenFastTestCase;

public class DeltaIntegerCodecTest extends OpenFastTestCase {
    Scalar noDefaultScalar = new Scalar("", Type.I32, new DeltaOperator(FastOperatorTestHarness.KEY, Fast.GLOBAL, null), true);
    Scalar defaultScalar = new Scalar("", Type.I32, new DeltaOperator(FastOperatorTestHarness.KEY, Fast.GLOBAL, "15"), true);
    FastOperatorTestHarness harness = new FastOperatorTestHarness(defaultScalar, noDefaultScalar);
    
    public void testDecode() {
        harness.assertDecodeNull(NO_INITIAL_VALUE, UNDEFINED, "10000000");
        harness.assertDecode(16, INITIAL_VALUE, UNDEFINED, "10000010");
        harness.assertDecode(1, NO_INITIAL_VALUE, UNDEFINED, "10000010");
        harness.assertDecode(30, NO_INITIAL_VALUE, 25, "10000110");
        harness.assertDecode(196, NO_INITIAL_VALUE, 200, "11111101");
    }

    public void testEncode() {
        harness.assertEncode("10000000", NO_INITIAL_VALUE, UNDEFINED);
        harness.assertEncode("10000010", NO_INITIAL_VALUE, UNDEFINED, 1);
        harness.assertEncode("10000011", INITIAL_VALUE, UNDEFINED, 17);
        harness.assertEncode("11111101", INITIAL_VALUE, 200, 196);
    }
}
