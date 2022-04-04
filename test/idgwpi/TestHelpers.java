package idgwpi;

public class TestHelpers {
    public static org.mockito.ArgumentMatcher<byte[][]> getMatcher(byte[][] expectedByteArray){
        return array -> {
            if (array.length != expectedByteArray.length)
                return false;
            var i = 0;
            var j = 0;
            for (byte[] bytes : array){
                if (bytes.length != expectedByteArray[i].length)
                    return false;
                for (byte b : bytes){
                    if (expectedByteArray[i][j++] != b)
                        return false;
                }
                j = 0;
                i++;
            }
            return true;
        };
    }
}
