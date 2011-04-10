package org.jboss.arquillian.ajocado.css;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Interface for definition of CSS properties. This interface simplifies storing of values in maps, and provides basic
 * inheritance support.
 * 
 * All implementations of this interface should provide static value with signature:
 * 
 * <pre>
 * <b>public static</b> CSSProperty valueOf(String value);
 * </pre>
 * 
 * to retrieve instance of property by string value. Since enum classes provides this value automatically, it is
 * encouraged to use them.
 * 
 * For make use of enums easier, this contract should be followed:
 * 
 * All values directly represented in CSS style sheet such as: <code>float: <b>left</b>;</code> or
 * <code>background-repeat: 
 * <b>repeat-x</b>;</code> should to converted to upper case and all not alphanumeric characters should be converted
 * into underscores (<code>_</code>), for example <code>REPEAT_X</code>
 * 
 * All other values, with essentially requires additional data, should broke enum standard and use lower case letters
 * only. This way it is guaranteed that this value won't never be considered as a keyword.
 * 
 * @author kapy
 * TODO consult with Karel
 * 
 */
public interface CssProperty {

    /**
     * CSS "inherit" keyword for retrieving instance by Translator object
     */
    public static final String INHERIT_KEYWORD = "INHERIT";

    public static final String FONT_SERIF = java.awt.Font.SERIF;
    public static final String FONT_SANS_SERIF = java.awt.Font.SANS_SERIF;
    public static final String FONT_MONOSPACED = java.awt.Font.MONOSPACED;
    public static final String FONT_CURSIVE = "Zapf-Chancery";
    public static final String FONT_FANTASY = "Western";

    /**
     * Allows declarations of properties to inherit or to be inherited
     * 
     * @return <code>true</code> in case that this property could be inherited from parent, <code>false</code> elsewhere
     */
    boolean couldBeInherited();

    /**
     * Allows to check whether property equals <code>inherit</code> value
     * 
     * @return <code>true</code>if value is <code>INHERIT</code>, <code>false</code> otherwise
     */
    boolean isInherited();

    /**
     * Textual representation of CSS property
     * 
     * @return String
     */
    String toString();

    /***************************************************************
     * TRANSLATOR *
     ****************************************************************/

    /**
     * Retrieves value of property of given class and text value
     * 
     * @author kapy
     * 
     */
    public static class Translator {

        /**
         * Methods cache
         */
        private static Map<Class<? extends CssProperty>, Method> translators = new HashMap<Class<? extends CssProperty>, Method>();

        /**
         * Retrieves CSSProperty by its name and class
         * 
         * @param <T>
         *            TODO
         * 
         * @param type
         *            Class of CSSProperty
         * @param value
         *            Text value
         * @return CSSProperty if found, <code>null</code> elsewhere
         */
        @SuppressWarnings("unchecked")
        public static final <T extends CssProperty> T valueOf(Class<T> type, String value) {
            try {
                Method m = translators.get(type);
                if (m == null) {
                    m = type.getMethod("valueOf", String.class);
                }
                return (T) m.invoke(null, value);
            } catch (Exception e) {
                return null;
                /*
                 * throw new IllegalArgumentException("Unable to get: " + value + " for: " + type.getName(), e);
                 */
            }
        }

        /**
         * Creates "inherit" instance
         * 
         * @param <T>
         *            TODO
         * 
         * @param type
         *            Type of CSS property
         * @return Should always return CSS instance. If <code>null</code> is returned, something is flawed.
         */
        public static final <T extends CssProperty> T createInherit(Class<T> type) {
            return valueOf(type, INHERIT_KEYWORD);
        }
    }

    /************************************************************************
     * CSS PROPERTIES *
     ************************************************************************/

    public enum Azimuth implements CssProperty {
        angle(""), LEFT_SIDE("left-side"), FAR_LEFT("far-left"), LEFT("left"), CENTER_LEFT("center-left"), CENTER(
            "center"), CENTER_RIGHT("center-right"), RIGHT("right"), FAR_RIGHT("far-right"), RIGHT_SIDE("right-side"), BEHIND(
            "behind"), LEFTWARDS("leftwards"), RIGHTWARDS("rightwards"), INHERIT("inherit");

        private String text;

        private Azimuth(String text) {
            this.text = text;
        }

        public boolean couldBeInherited() {
            return true;
        }

        public boolean isInherited() {
            return this == INHERIT;
        }

        @Override
        public String toString() {
            return text;
        }

    }

    public enum Color implements CssProperty {
        color(""), INHERIT("inherit");

        private String text;

        private Color(String text) {
            this.text = text;
        }

        public boolean couldBeInherited() {
            return true;
        }

        public boolean isInherited() {
            return this == INHERIT;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public enum Cue implements CssProperty {
        component_values(""), uri(""), NONE("none"), INHERIT("inherit");

        private String text;

        private Cue(String text) {
            this.text = text;
        }

        public boolean couldBeInherited() {
            return false;
        }

        public boolean isInherited() {
            return this == INHERIT;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public enum Background implements CssProperty {
        component_values(""), INHERIT("inherit");

        private String text;

        private Background(String text) {
            this.text = text;
        }

        public boolean couldBeInherited() {
            return false;
        }

        public boolean isInherited() {
            return this == INHERIT;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public enum BackgroundAttachment implements CssProperty {
        SCROLL("scroll"), FIXED("fixed"), INHERIT("inherit");

        private String text;

        private BackgroundAttachment(String text) {
            this.text = text;
        }

        public boolean couldBeInherited() {
            return false;
        }

        public boolean isInherited() {
            return this == INHERIT;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public enum BackgroundColor implements CssProperty {
        color(""), TRANSPARENT("trasparent"), INHERIT("inherit");

        private String text;

        private BackgroundColor(String text) {
            this.text = text;
        }

        public boolean couldBeInherited() {
            return false;
        }

        public boolean isInherited() {
            return this == INHERIT;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public enum BackgroundImage implements CssProperty {
        uri(""), NONE("none"), INHERIT("inherit");

        private String text;

        private BackgroundImage(String text) {
            this.text = text;
        }

        public boolean couldBeInherited() {
            return false;
        }

        public boolean isInherited() {
            return this == INHERIT;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public enum BackgroundPosition implements CssProperty {
        list_values(""), LEFT("left"), CENTER("center"), RIGHT("right"), INHERIT("inherit");

        private String text;

        private BackgroundPosition(String text) {
            this.text = text;
        }

        public boolean couldBeInherited() {
            return false;
        }

        public boolean isInherited() {
            return this == INHERIT;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public enum BackgroundRepeat implements CssProperty {
        REPEAT("repeat"), REPEAT_X("repeat-x"), REPEAT_Y("repeat-y"), NO_REPEAT("no-repeat"), INHERIT("inherit");

        private String text;

        private BackgroundRepeat(String text) {
            this.text = text;
        }

        public boolean couldBeInherited() {
            return false;
        }

        public boolean isInherited() {
            return this == INHERIT;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public enum Border implements CssProperty {
        component_values(""), INHERIT("inherit");

        private String text;

        private Border(String text) {
            this.text = text;
        }

        public boolean couldBeInherited() {
            return false;
        }

        public boolean isInherited() {
            return this == INHERIT;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public enum BorderCollapse implements CssProperty {
        COLLAPSE("collapse"), SEPARATE("separate"), INHERIT("inherit");

        private String text;

        private BorderCollapse(String text) {
            this.text = text;
        }

        public boolean couldBeInherited() {
            return false;
        }

        public boolean isInherited() {
            return this == INHERIT;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public enum BorderColor implements CssProperty {
        color(""), taken(""), component_values(""), TRANSPARENT("transparent"), INHERIT("inherit");

        private String text;

        private BorderColor(String text) {
            this.text = text;
        }

        public boolean couldBeInherited() {
            return false;
        }

        public boolean isInherited() {
            return this == INHERIT;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public enum BorderSpacing implements CssProperty {
        list_values(""), INHERIT("inherit");

        private String text;

        private BorderSpacing(String text) {
            this.text = text;
        }

        public boolean couldBeInherited() {
            return true;
        }

        public boolean isInherited() {
            return this == INHERIT;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public enum BorderStyle implements CssProperty {
        component_values(""), NONE("none"), HIDDEN("hidden"), DOTTED("dotted"), DASHED("dashed"), SOLID("solid"), DOUBLE(
            "double"), GROOVE("groove"), RIDGE("ridge"), INSET("inset"), OUTSET("outset"), INHERIT("inherit");

        private String text;

        private BorderStyle(String text) {
            this.text = text;
        }

        public boolean couldBeInherited() {
            return false;
        }

        public boolean isInherited() {
            return this == INHERIT;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public enum BorderWidth implements CssProperty {
        component_values(""), length(""), THIN("thin"), MEDIUM("medium"), THICK("thick"), INHERIT("inherit");

        private String text;

        private BorderWidth(String text) {
            this.text = text;
        }

        public boolean couldBeInherited() {
            return false;
        }

        public boolean isInherited() {
            return this == INHERIT;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public enum Elevation implements CssProperty {
        angle(""), BELOW("below"), LEVEL("level"), ABOVE("above"), HIGHER("higher"), LOWER("lower"), INHERIT("inherit");

        private String text;

        private Elevation(String text) {
            this.text = text;
        }

        public boolean couldBeInherited() {
            return true;
        }

        public boolean isInherited() {
            return this == INHERIT;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public enum Font implements CssProperty {
        component_values(""), CAPTION("caption"), ICON("icon"), MENU("menu"), MESSAGE_BOX("message-box"), SMALL_CAPTION(
            "small-caption"), STATUS_BAR("status-bar"), INHERIT("inherit");

        private String text;

        private Font(String text) {
            this.text = text;
        }

        public boolean couldBeInherited() {
            return true;
        }

        public boolean isInherited() {
            return this == INHERIT;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public enum FontFamily implements CssProperty {
        list_values("", ""), SERIF("serif", FONT_SERIF), SANS_SERIF("sans-serif", FONT_SANS_SERIF), CURSIVE("cursive",
            FONT_CURSIVE), FANTASY("fantasy", FONT_FANTASY), MONOSPACE("monospace", FONT_MONOSPACED), INHERIT(
            "inherit", "");

        private String text;
        private String awtval;

        private FontFamily(String text, String awtval) {
            this.text = text;
            this.awtval = awtval;
        }

        public boolean couldBeInherited() {
            return true;
        }

        public boolean isInherited() {
            return this == INHERIT;
        }

        @Override
        public String toString() {
            return text;
        }

        public String getAWTValue() {
            return awtval;
        }
    }

    public enum FontSize implements CssProperty {
        percentage(""), length(""), XX_SMALL("xx-small"), X_SMALL("x-small"), SMALL("small"), MEDIUM("medium"), LARGE(
            "large"), X_LARGE("x-large"), XX_LARGE("xx-large"), LARGER("larger"), SMALLER("smaller"), INHERIT("inherit");

        private String text;

        private FontSize(String text) {
            this.text = text;
        }

        public boolean couldBeInherited() {
            return false;
        }

        public boolean isInherited() {
            return this == INHERIT;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public enum FontStyle implements CssProperty {
        NORMAL("normal"), ITALIC("italic"), OBLIQUE("oblique"), INHERIT("inherit");

        private String text;

        private FontStyle(String text) {
            this.text = text;
        }

        public boolean couldBeInherited() {
            return true;
        }

        public boolean isInherited() {
            return this == INHERIT;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public enum FontVariant implements CssProperty {
        SMALL_CAPS("small-caps"), NORMAL("normal"), INHERIT("inherit");

        private String text;

        private FontVariant(String text) {
            this.text = text;
        }

        public boolean couldBeInherited() {
            return true;
        }

        public boolean isInherited() {
            return this == INHERIT;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public enum FontWeight implements CssProperty {
        numeric_100("100"), numeric_200("200"), numeric_300("300"), numeric_400("400"), numeric_500("500"), numeric_600(
            "600"), numeric_700("700"), numeric_800("800"), numeric_900("900"), NORMAL("normal"), BOLD("bold"), BOLDER(
            "bolder"), LIGHTER("lighter"), INHERIT("inherit");

        private String text;

        private FontWeight(String text) {
            this.text = text;
        }

        public boolean couldBeInherited() {
            return true;
        }

        public boolean isInherited() {
            return this == INHERIT;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public enum LineHeight implements CssProperty {
        number(""), length(""), percentage(""), NORMAL("normal"), INHERIT("inherit");

        private String text;

        private LineHeight(String text) {
            this.text = text;
        }

        public boolean couldBeInherited() {
            return true;
        }

        public boolean isInherited() {
            return this == INHERIT;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public enum CaptionSide implements CssProperty {
        TOP("top"), BOTTOM("bottom"), INHERIT("inherit");

        private String text;

        private CaptionSide(String text) {
            this.text = text;
        }

        public boolean couldBeInherited() {
            return true;
        }

        public boolean isInherited() {
            return this == INHERIT;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public enum Content implements CssProperty {
        list_values(""), NORMAL("normal"), NONE("none"), INHERIT("inherit");

        private String text;

        private Content(String text) {
            this.text = text;
        }

        public boolean couldBeInherited() {
            return false;
        }

        public boolean isInherited() {
            return this == INHERIT;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public enum CounterIncrement implements CssProperty {
        list_values(""), NONE("none"), INHERIT("inherit");

        private String text;

        private CounterIncrement(String text) {
            this.text = text;
        }

        public boolean couldBeInherited() {
            return false;
        }

        public boolean isInherited() {
            return this == INHERIT;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public enum CounterReset implements CssProperty {
        list_values(""), NONE("none"), INHERIT("inherit");

        private String text;

        private CounterReset(String text) {
            this.text = text;
        }

        public boolean couldBeInherited() {
            return false;
        }

        public boolean isInherited() {
            return this == INHERIT;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public enum Clear implements CssProperty {
        NONE("none"), LEFT("left"), RIGHT("right"), BOTH("both"), INHERIT("inherit");

        private String text;

        private Clear(String text) {
            this.text = text;
        }

        public boolean couldBeInherited() {
            return false;
        }

        public boolean isInherited() {
            return this == INHERIT;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public enum Clip implements CssProperty {
        rect(""), AUTO("auto"), INHERIT("inherit");

        private String text;

        private Clip(String text) {
            this.text = text;
        }

        public boolean couldBeInherited() {
            return false;
        }

        public boolean isInherited() {
            return this == INHERIT;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public enum Cursor implements CssProperty {
        AUTO("auto"), CROSSHAIR("crosshair"), DEFAULT("default"), POINTER("pointer"), MOVE("move"), E_RESIZE("e-resize"), NE_RESIZE(
            "ne-resize"), NW_RESIZE("nw-resize"), N_RESIZE("n-resize"), SE_RESIZE("se-resize"), SW_RESIZE("sw-resize"), S_RESIZE(
            "s-resize"), W_RESIZE("w-resize"), TEXT("text"), WAIT("wait"), PROGRESS("progress"), HELP("help"), INHERIT(
            "inherit");

        private String text;

        private Cursor(String text) {
            this.text = text;
        }

        public boolean couldBeInherited() {
            return true;
        }

        public boolean isInherited() {
            return this == INHERIT;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public enum Direction implements CssProperty {
        LTR("ltr"), RTL("rtl"), INHERIT("inherit");

        private String text;

        private Direction(String text) {
            this.text = text;
        }

        public boolean couldBeInherited() {
            return true;
        }

        public boolean isInherited() {
            return this == INHERIT;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public enum Display implements CssProperty {
        INLINE("inline"), BLOCK("block"), LIST_ITEM("list-item"), RUN_IN("run-in"), INLINE_BLOCK("inline-block"), TABLE(
            "table"), INLINE_TABLE("inline-table"), TABLE_ROW_GROUP("table-row-group"), TABLE_HEADER_GROUP(
            "table-header-group"), TABLE_FOOTER_GROUP("table-footer-group"), TABLE_ROW("table-row"), TABLE_COLUMN_GROUP(
            "table-column-group"), TABLE_COLUMN("table-column"), TABLE_CELL("table-cell"), TABLE_CAPTION(
            "table-caption"), NONE("none"), INHERIT("inherit");

        private String text;

        private Display(String text) {
            this.text = text;
        }

        public boolean couldBeInherited() {
            return false;
        }

        public boolean isInherited() {
            return this == INHERIT;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public enum Width implements CssProperty {
        length(""), percentage(""), AUTO("auto"), INHERIT("inherit");

        private String text;

        private Width(String text) {
            this.text = text;
        }

        public boolean couldBeInherited() {
            return false;
        }

        public boolean isInherited() {
            return this == INHERIT;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public enum MinWidth implements CssProperty {
        length(""), percentage(""), INHERIT("inherit");

        private String text;

        private MinWidth(String text) {
            this.text = text;
        }

        public boolean couldBeInherited() {
            return false;
        }

        public boolean isInherited() {
            return this == INHERIT;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public enum MaxWidth implements CssProperty {
        length(""), percentage(""), NONE("none"), INHERIT("inherit");

        private String text;

        private MaxWidth(String text) {
            this.text = text;
        }

        public boolean couldBeInherited() {
            return false;
        }

        public boolean isInherited() {
            return this == INHERIT;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public enum Height implements CssProperty {
        length(""), percentage(""), AUTO("auto"), INHERIT("inherit");

        private String text;

        private Height(String text) {
            this.text = text;
        }

        public boolean couldBeInherited() {
            return false;
        }

        public boolean isInherited() {
            return this == INHERIT;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public enum MinHeight implements CssProperty {
        length(""), percentage(""), INHERIT("inherit");

        private String text;

        private MinHeight(String text) {
            this.text = text;
        }

        public boolean couldBeInherited() {
            return false;
        }

        public boolean isInherited() {
            return this == INHERIT;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public enum MaxHeight implements CssProperty {
        length(""), percentage(""), NONE("none"), INHERIT("inherit");

        private String text;

        private MaxHeight(String text) {
            this.text = text;
        }

        public boolean couldBeInherited() {
            return false;
        }

        public boolean isInherited() {
            return this == INHERIT;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public enum EmptyCells implements CssProperty {
        SHOW("show"), HIDE("hide"), INHERIT("inherit");

        private String text;

        private EmptyCells(String text) {
            this.text = text;
        }

        public boolean couldBeInherited() {
            return true;
        }

        public boolean isInherited() {
            return this == INHERIT;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public enum Float implements CssProperty {
        NONE("none"), LEFT("left"), RIGHT("right"), INHERIT("inherit");

        private String text;

        private Float(String text) {
            this.text = text;
        }

        public boolean couldBeInherited() {
            return false;
        }

        public boolean isInherited() {
            return this == INHERIT;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public enum ListStyle implements CssProperty {
        component_values(""), INHERIT("inherit");

        private String text;

        private ListStyle(String text) {
            this.text = text;
        }

        public boolean couldBeInherited() {
            return true;
        }

        public boolean isInherited() {
            return this == INHERIT;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public enum ListStyleImage implements CssProperty {
        uri(""), NONE("none"), INHERIT("inherit");

        private String text;

        private ListStyleImage(String text) {
            this.text = text;
        }

        public boolean couldBeInherited() {
            return true;
        }

        public boolean isInherited() {
            return this == INHERIT;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public enum ListStylePosition implements CssProperty {
        INSIDE("inside"), OUTSIDE("outside"), INHERIT("inherit");

        private String text;

        private ListStylePosition(String text) {
            this.text = text;
        }

        public boolean couldBeInherited() {
            return true;
        }

        public boolean isInherited() {
            return this == INHERIT;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public enum ListStyleType implements CssProperty {
        DISC("disc"), CIRCLE("circle"), SQUARE("square"), DECIMAL("decimal"), DECIMAL_LEADING_ZERO(
            "decimal-leading-zero"), LOWER_ROMAN("lower-roman"), UPPER_ROMAN("upper-roman"), LOWER_GREEK("lower-greek"), LOWER_LATIN(
            "lower-latin"), UPPER_LATN("upper-latin"), ARMENIAN("armenian"), GEORGIAN("georgian"), LOWER_ALPHA(
            "lower-alpha"), UPPER_ALPHA("upper-alpha"), NONE("none"), INHERIT("inherit");

        private String text;

        private ListStyleType(String text) {
            this.text = text;
        }

        public boolean couldBeInherited() {
            return true;
        }

        public boolean isInherited() {
            return this == INHERIT;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public enum Margin implements CssProperty {
        length(""), percentage(""), component_values(""), AUTO("auto"), INHERIT("inherit");

        private String text;

        private Margin(String text) {
            this.text = text;
        }

        public boolean couldBeInherited() {
            return false;
        }

        public boolean isInherited() {
            return this == INHERIT;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public enum Orphans implements CssProperty {
        integer(""), INHERIT("inherit");

        private String text;

        private Orphans(String text) {
            this.text = text;
        }

        public boolean couldBeInherited() {
            return true;
        }

        public boolean isInherited() {
            return this == INHERIT;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public enum Outline implements CssProperty {
        component_values(""), INHERIT("inherit");

        private String text;

        private Outline(String text) {
            this.text = text;
        }

        public boolean couldBeInherited() {
            return false;
        }

        public boolean isInherited() {
            return this == INHERIT;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public enum OutlineWidth implements CssProperty {
        length(""), THIN("thin"), MEDIUM("medium"), THICK("thick"), INHERIT("inherit");

        private String text;

        private OutlineWidth(String text) {
            this.text = text;
        }

        public boolean couldBeInherited() {
            return false;
        }

        public boolean isInherited() {
            return this == INHERIT;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public enum OutlineStyle implements CssProperty {
        NONE("none"), DOTTED("dotted"), DASHED("dashed"), SOLID("solid"), DOUBLE("double"), GROOVE("groove"), RIDGE(
            "ridge"), INSET("inset"), OUTSET("outset"), HIDDEN("hidden"), INHERIT("inherit");

        private String text;

        private OutlineStyle(String text) {
            this.text = text;
        }

        public boolean couldBeInherited() {
            return false;
        }

        public boolean isInherited() {
            return this == INHERIT;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public enum OutlineColor implements CssProperty {
        color(""), INVERT("invert"), INHERIT("inherit");

        private String text;

        private OutlineColor(String text) {
            this.text = text;
        }

        public boolean couldBeInherited() {
            return false;
        }

        public boolean isInherited() {
            return this == INHERIT;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public enum Overflow implements CssProperty {
        VISIBLE("visible"), HIDDEN("hidden"), SCROLL("scroll"), AUTO("auto"), INHERIT("inherit");

        private String text;

        private Overflow(String text) {
            this.text = text;
        }

        public boolean couldBeInherited() {
            return false;
        }

        public boolean isInherited() {
            return this == INHERIT;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public enum Padding implements CssProperty {
        length(""), percentage(""), component_values(""), AUTO("auto"), INHERIT("inherit");

        private String text;

        private Padding(String text) {
            this.text = text;
        }

        public boolean couldBeInherited() {
            return false;
        }

        public boolean isInherited() {
            return this == INHERIT;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public enum PageBreak implements CssProperty {
        AUTO("auto"), ALWAYS("always"), AVOID("avoid"), LEFT("left"), RIGHT("right"), INHERIT("inherit");

        private String text;

        private PageBreak(String text) {
            this.text = text;
        }

        public boolean couldBeInherited() {
            return false;
        }

        public boolean isInherited() {
            return this == INHERIT;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public enum PageBreakInside implements CssProperty {
        AUTO("auto"), AVOID("avoid"), INHERIT("inherit");

        private String text;

        private PageBreakInside(String text) {
            this.text = text;
        }

        public boolean couldBeInherited() {
            return true;
        }

        public boolean isInherited() {
            return this == INHERIT;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public enum Pause implements CssProperty {
        component_values(""), time(""), percentage(""), INHERIT("inherit");

        private String text;

        private Pause(String text) {
            this.text = text;
        }

        public boolean couldBeInherited() {
            return true;
        }

        public boolean isInherited() {
            return this == INHERIT;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public enum PitchRange implements CssProperty {
        number(""), INHERIT("inherit");

        private String text;

        private PitchRange(String text) {
            this.text = text;
        }

        public boolean couldBeInherited() {
            return true;
        }

        public boolean isInherited() {
            return this == INHERIT;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public enum Pitch implements CssProperty {
        frequency(""), X_LOW("x-low"), LOW("low"), MEDIUM("medium"), HIGH("high"), X_HIGH("x-high"), INHERIT("inherit");

        private String text;

        private Pitch(String text) {
            this.text = text;
        }

        public boolean couldBeInherited() {
            return true;
        }

        public boolean isInherited() {
            return this == INHERIT;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public enum PlayDuring implements CssProperty {
        uri(""), uri_mix(""), uri_repeat(""), AUTO("auto"), NONE("none"), INHERIT("inherit");

        private String text;

        private PlayDuring(String text) {
            this.text = text;
        }

        public boolean couldBeInherited() {
            return false;
        }

        public boolean isInherited() {
            return this == INHERIT;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public enum Position implements CssProperty {
        STATIC("static"), RELATIVE("relative"), ABSOLUTE("absolute"), FIXED("fixed"), INHERIT("inherit");

        private String text;

        private Position(String text) {
            this.text = text;
        }

        public boolean couldBeInherited() {
            return false;
        }

        public boolean isInherited() {
            return this == INHERIT;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public enum Richness implements CssProperty {
        number("number"), INHERIT("inherit");

        private String text;

        private Richness(String text) {
            this.text = text;
        }

        public boolean couldBeInherited() {
            return true;
        }

        public boolean isInherited() {
            return this == INHERIT;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public enum SpeakHeader implements CssProperty {
        ONCE("once"), ALWAYS("always"), INHERIT("inherit");

        private String text;

        private SpeakHeader(String text) {
            this.text = text;
        }

        public boolean couldBeInherited() {
            return true;
        }

        public boolean isInherited() {
            return this == INHERIT;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public enum SpeakNumeral implements CssProperty {
        DIGITS("digits"), CONTINUOUS("continuous"), INHERIT("inherit");

        private String text;

        private SpeakNumeral(String text) {
            this.text = text;
        }

        public boolean couldBeInherited() {
            return true;
        }

        public boolean isInherited() {
            return this == INHERIT;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public enum SpeakPunctuation implements CssProperty {
        CODE("code"), NONE("none"), INHERIT("inherit");

        private String text;

        private SpeakPunctuation(String text) {
            this.text = text;
        }

        public boolean couldBeInherited() {
            return true;
        }

        public boolean isInherited() {
            return this == INHERIT;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public enum Speak implements CssProperty {
        NORMAL("normal"), NONE("none"), SPELL_OUT("spell-out"), INHERIT("inherit");

        private String text;

        private Speak(String text) {
            this.text = text;
        }

        public boolean couldBeInherited() {
            return true;
        }

        public boolean isInherited() {
            return this == INHERIT;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public enum SpeechRate implements CssProperty {
        number(""), X_SLOW("x-slow"), SLOW("slow"), MEDIUM("medium"), FAST("fast"), X_FAST("x-fast"), FASTER("faster"), SLOWER(
            "slower"), INHERIT("inherit");

        private String text;

        private SpeechRate(String text) {
            this.text = text;
        }

        public boolean couldBeInherited() {
            return true;
        }

        public boolean isInherited() {
            return this == INHERIT;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public enum Stress implements CssProperty {
        number(""), INHERIT("inherit");

        private String text;

        private Stress(String text) {
            this.text = text;
        }

        public boolean couldBeInherited() {
            return true;
        }

        public boolean isInherited() {
            return this == INHERIT;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public enum Top implements CssProperty {
        length(""), percentage(""), AUTO("auto"), INHERIT("inherit");

        private String text;

        private Top(String text) {
            this.text = text;
        }

        public boolean couldBeInherited() {
            return false;
        }

        public boolean isInherited() {
            return this == INHERIT;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public enum Right implements CssProperty {
        length(""), percentage(""), AUTO("auto"), INHERIT("inherit");

        private String text;

        private Right(String text) {
            this.text = text;
        }

        public boolean couldBeInherited() {
            return false;
        }

        public boolean isInherited() {
            return this == INHERIT;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public enum Bottom implements CssProperty {
        length(""), percentage(""), AUTO("auto"), INHERIT("inherit");

        private String text;

        private Bottom(String text) {
            this.text = text;
        }

        public boolean couldBeInherited() {
            return false;
        }

        public boolean isInherited() {
            return this == INHERIT;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public enum Left implements CssProperty {
        length(""), percentage(""), AUTO("auto"), INHERIT("inherit");

        private String text;

        private Left(String text) {
            this.text = text;
        }

        public boolean couldBeInherited() {
            return false;
        }

        public boolean isInherited() {
            return this == INHERIT;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public enum Quotes implements CssProperty {
        list_values(""), NONE("none"), INHERIT("inherit");

        private String text;

        private Quotes(String text) {
            this.text = text;
        }

        public boolean couldBeInherited() {
            return true;
        }

        public boolean isInherited() {
            return this == INHERIT;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public enum TableLayout implements CssProperty {
        AUTO("auto"), FIXED("fixed"), INHERIT("inherit");

        private String text;

        private TableLayout(String text) {
            this.text = text;
        }

        public boolean couldBeInherited() {
            return false;
        }

        public boolean isInherited() {
            return this == INHERIT;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public enum TextAlign implements CssProperty {
        BY_DIRECTION("by-direction"), LEFT("left"), RIGHT("right"), CENTER("center"), JUSTIFY("justify"), INHERIT(
            "inherit");

        private String text;

        private TextAlign(String text) {
            this.text = text;
        }

        public boolean couldBeInherited() {
            return true;
        }

        public boolean isInherited() {
            return this == INHERIT;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public enum TextDecoration implements CssProperty {
        list_values(""), UNDERLINE("underline"), OVERLINE("overline"), BLINK("blink"), LINE_THROUGH("line-through"), NONE(
            "none"), INHERIT("inherit");

        private String text;

        private TextDecoration(String text) {
            this.text = text;
        }

        public boolean couldBeInherited() {
            return false;
        }

        public boolean isInherited() {
            return this == INHERIT;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public enum TextIndent implements CssProperty {
        length(""), percentage(""), INHERIT("inherit");

        private String text;

        private TextIndent(String text) {
            this.text = text;
        }

        public boolean couldBeInherited() {
            return true;
        }

        public boolean isInherited() {
            return this == INHERIT;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public enum TextTransform implements CssProperty {
        CAPITALIZE("capitalize"), UPPERCASE("uppercase"), LOWERCASE("lowercase"), NONE("none"), INHERIT("inherit");

        private String text;

        private TextTransform(String text) {
            this.text = text;
        }

        public boolean couldBeInherited() {
            return true;
        }

        public boolean isInherited() {
            return this == INHERIT;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public enum UnicodeBidi implements CssProperty {
        NORMAL("normal"), EMDEB("embed"), BIDI_OVERRIDE("bidi-override"), INHERIT("inherit");

        private String text;

        private UnicodeBidi(String text) {
            this.text = text;
        }

        public boolean couldBeInherited() {
            return false;
        }

        public boolean isInherited() {
            return this == INHERIT;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public enum VerticalAlign implements CssProperty {
        length(""), percentage(""), BASELINE("baseline"), SUB("sub"), SUPER("super"), TOP("top"), TEXT_TOP("text-top"), MIDDLE(
            "middle"), BOTTOM("bottom"), TEXT_BOTTOM("text-bottom"), INHERIT("inherit");

        private String text;

        private VerticalAlign(String text) {
            this.text = text;
        }

        public boolean couldBeInherited() {
            return false;
        }

        public boolean isInherited() {
            return this == INHERIT;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public enum Visibility implements CssProperty {
        VISIBLE("visible"), HIDDEN("hidden"), COLLAPSE("collapse"), INHERIT("inherit");

        private String text;

        private Visibility(String text) {
            this.text = text;
        }

        public boolean couldBeInherited() {
            return true;
        }

        public boolean isInherited() {
            return this == INHERIT;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public enum VoiceFamily implements CssProperty {
        list_values(""), MALE("male"), FEMALE("female"), CHILD("child"), INHERIT("inherit");

        private String text;

        private VoiceFamily(String text) {
            this.text = text;
        }

        public boolean couldBeInherited() {
            return true;
        }

        public boolean isInherited() {
            return this == INHERIT;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public enum Volume implements CssProperty {
        number(""), percentage(""), SILENT("silent"), X_SOFT("x-soft"), SOFT("soft"), MEDIUM("medium"), LOUD("loud"), X_LOUD(
            "x-loud"), INHERIT("inherit");

        private String text;

        private Volume(String text) {
            this.text = text;
        }

        public boolean couldBeInherited() {
            return true;
        }

        public boolean isInherited() {
            return this == INHERIT;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public enum WhiteSpace implements CssProperty {
        NORMAL("normal"), PRE("pre"), NOWRAP("nowrap"), PRE_WRAP("pre-wrap"), PRE_LINE("pre-line"), INHERIT("inherit");

        private String text;

        private WhiteSpace(String text) {
            this.text = text;
        }

        public boolean couldBeInherited() {
            return true;
        }

        public boolean isInherited() {
            return this == INHERIT;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public enum Widows implements CssProperty {
        integer(""), INHERIT("inherit");

        private String text;

        private Widows(String text) {
            this.text = text;
        }

        public boolean couldBeInherited() {
            return true;
        }

        public boolean isInherited() {
            return this == INHERIT;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public enum WordSpacing implements CssProperty {
        length(""), NORMAL("normal"), INHERIT("inherit");

        private String text;

        private WordSpacing(String text) {
            this.text = text;
        }

        public boolean couldBeInherited() {
            return true;
        }

        public boolean isInherited() {
            return this == INHERIT;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public enum LetterSpacing implements CssProperty {
        length(""), NORMAL("normal"), INHERIT("inherit");

        private String text;

        private LetterSpacing(String text) {
            this.text = text;
        }

        public boolean couldBeInherited() {
            return true;
        }

        public boolean isInherited() {
            return this == INHERIT;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public enum ZIndex implements CssProperty {
        integer(""), AUTO("auto"), INHERIT("inherit");

        private String text;

        private ZIndex(String text) {
            this.text = text;
        }

        public boolean couldBeInherited() {
            return false;
        }

        public boolean isInherited() {
            return this == INHERIT;
        }

        @Override
        public String toString() {
            return text;
        }
    }

}