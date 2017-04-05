package org.wltea.analyzer.db;

/**
 * 词的类型枚举定义。
 * Created by ginozhang on 2017/4/5.
 */
public enum WordType {

    MainWord(1), StopWord(2), SurnameWord(3), QuantifierWord(4), SuffixWord(5), PrepositionWord(6);

    private WordType(int code) {
        this.code = code;
    }

    private int code;

    public int getCode() {
        return this.code;
    }

    public static WordType fromCode(int code) {
        for (WordType wordType : WordType.values()) {
            if (code == wordType.code) {
                return wordType;
            }
        }

        return null;
    }

    public static String getCodes() {
        StringBuffer sb = new StringBuffer(10);
        for (WordType wordType : WordType.values()) {
            if (sb.length() > 0) {
                sb.append(",");
            }

            sb.append(wordType.getCode());
        }

        return sb.toString();
    }
}
