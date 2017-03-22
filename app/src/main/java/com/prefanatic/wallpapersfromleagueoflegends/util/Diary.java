package com.prefanatic.wallpapersfromleagueoflegends.util;
import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Logging class used to simply write things.
 * <p>
 * A Diary contains {@link Page Pages}, where each Page can be written to independently.
 * A Diary can write to all of its pages at once, or string individual pages, generated from {@link #addPage(Page)}
 * <p>
 * The Diary can write in {@link #ASSERT}, {@link #DEBUG}, {@link #ERROR}, {@link #INFO}, {@link #VERBOSE}, and {@link #WARN}
 */
public class Diary {
    public static final int ASSERT = 7;
    public static final int DEBUG = 3;
    public static final int ERROR = 6;
    public static final int INFO = 4;
    public static final int VERBOSE = 2;
    public static final int WARN = 5;

    /**
     * Index of a {@link StackTraceElement} array, to determine the calling class used.
     */
    private static final int STACK_INDEX = 5;

    private static volatile List<Page> pages = new ArrayList<>();

    private Diary() {
    }

    /**
     * Adds a {@link Page} into the Diary.
     *
     * @param page {@link Page}
     */
    public static void addPage(Page page) {
        pages.add(page);
    }

    /**
     * Manually sets the next log call on all pages string the specified tag.
     * This is a one-time operation, and only the subsequent log call will contain this tag.
     *
     * @param tag Tag
     * @return {@link Diary}
     */
    public static Page tag(String tag) {
        TABLE_OF_CONTENTS.tag(tag);

        return TABLE_OF_CONTENTS;
    }

    /**
     * Writes an {@link #ASSERT} entry.
     *
     * @param msg  The message to log.
     * @param args Supplemented arguments to add into the log.
     */
    public static void a(String msg, Object... args) {
        TABLE_OF_CONTENTS.a(msg, args);
    }

    /**
     * Writes a {@link #DEBUG} entry.
     *
     * @param msg  The message to log.
     * @param args Supplemented arguments to add into the log.
     */
    public static void d(String msg, Object... args) {
        TABLE_OF_CONTENTS.d(msg, args);
    }

    /**
     * Writes an {@link #ERROR} entry.
     *
     * @param e    Exception to log.
     * @param msg  The message to log.
     * @param args Supplemented arguments to add into the log.
     */
    public static void e(Throwable e, String msg, Object... args) {
        TABLE_OF_CONTENTS.e(e, msg, args);
    }

    /**
     * Writes an {@link #ERROR} entry.
     *
     * @param msg  The message to log.
     * @param args Supplemented arguments to add into the log.
     */
    public static void e(String msg, Object... args) {
        TABLE_OF_CONTENTS.e(msg, args);
    }

    /**
     * Writes an {@link #INFO} entry.
     *
     * @param msg  The message to log.
     * @param args Supplemented arguments to add into the log.
     */
    public static void i(String msg, Object... args) {
        TABLE_OF_CONTENTS.i(msg, args);
    }

    /**
     * Writes a {@link #VERBOSE} entry.
     *
     * @param msg  The message to log.
     * @param args Supplemented arguments to add into the log.
     */
    public static void v(String msg, Object... args) {
        TABLE_OF_CONTENTS.v(msg, args);
    }

    /**
     * Writes a {@link #WARN} entry.
     *
     * @param msg  The message to log.
     * @param args Supplemented arguments to add into the log.
     */
    public static void w(String msg, Object... args) {
        TABLE_OF_CONTENTS.w(msg, args);
    }

    /**
     * A {@link Page} that handles the passing of logging into all pages in the Diary.
     */
    private static final Page TABLE_OF_CONTENTS = new Page(null, null, false) {
        @Override
        public Page tag(String tag) {
            for (Page page : pages)
                page.tag(tag);

            return this;
        }

        @Override
        public void a(String msg, Object... args) {
            for (Page page : pages)
                page.a(msg, args);
        }

        @Override
        public void d(String msg, Object... args) {
            for (Page page : pages)
                page.d(msg, args);
        }

        @Override
        public void e(Throwable e, String msg, Object... args) {
            for (Page page : pages)
                page.e(e, msg, args);
        }

        @Override
        public void e(String msg, Object... args) {
            for (Page page : pages)
                page.e(msg, args);
        }

        @Override
        public void i(String msg, Object... args) {
            for (Page page : pages)
                page.i(msg, args);
        }

        @Override
        public void v(String msg, Object... args) {
            for (Page page : pages)
                page.v(msg, args);
        }

        @Override
        public void w(String msg, Object... args) {
            for (Page page : pages)
                page.w(msg, args);
        }
    };

    /**
     * The Page is responsible for choosing a {@link Pen} and a {@link Style} for writing in the {@link Diary}.
     */
    public static class Page {
        private static final Pattern ANON_CLASS = Pattern.compile("(\\$\\d+)+$");

        private final Pen pen;
        private final Style style;
        private final boolean withCallingClass;
        private final Evaluate evaluation;

        private String overrideTag;

        /**
         * Constructor for the Pen.
         *
         * @param pen              Pen to be used.
         * @param style            Style to be used.
         * @param withCallingClass If the Page should show classes or not.
         */
        private Page(Pen pen, Style style, boolean withCallingClass) {
            this(pen, style, withCallingClass, null);
        }

        /**
         * Constructor for the Pen.
         *
         * @param pen              Pen to be used.
         * @param style            Style to be used.
         * @param withCallingClass If the Page should show classes or not.
         * @param evaluation       Evaluation logic to determine if logging should or should not happen.
         */
        private Page(Pen pen, Style style, boolean withCallingClass, Evaluate evaluation) {
            this.pen = pen;
            this.style = style;
            this.withCallingClass = withCallingClass;
            this.evaluation = evaluation;
        }

        /**
         * Parses a Throwable and returns the full stacktrace as a String.
         *
         * @param t Throwable to parse.
         * @return String of the stacktrace.
         */
        public static String getStackTrace(Throwable t) {
            // Taken out of the Android SDK.
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            t.printStackTrace(pw);
            pw.flush();

            return sw.toString();
        }

        /**
         * Returns the Page's {@link Pen}
         *
         * @return Pen
         */
        public Pen getPen() {
            return pen;
        }

        /**
         * Parses a new Throwable for a stacktrace, to retrieve the calling class.
         * Returns a cleaned class name, at the end of the package.  Also, will parse any anon classes.
         *
         * @return String of class name.
         */
        private String getCallingClassName() {
            // Grab our stack trace.
            StackTraceElement[] elements = new Throwable().getStackTrace();
            if (elements.length < STACK_INDEX) {
                throw new RuntimeException("Unable to access stacktrace elements for calling class.");
            }

            // Get the class name as per the STACK_INDEX, and then parse it.
            String className = elements[STACK_INDEX].getClassName();
            Matcher matcher = ANON_CLASS.matcher(className);
            if (matcher.find()) {
                className = matcher.replaceAll("");
            }

            // Return the class name, and not the full package.
            return className.substring(className.lastIndexOf(".") + 1);
        }

        /**
         * Main heavy-lifter for writing string our pen, and styling string our styles!
         *
         * @param mode Log mode.
         * @param msg  Message to write.
         * @param args Arguments to supplement.
         */
        private void write(int mode, Throwable throwable, String msg, Object... args) {
            if (pen == null) {
                throw new RuntimeException("There is no pen for us to write string!");
            }
            if (evaluation != null && !evaluation.run(mode))
                return; // Don't run if they don't want us to!

            String tag = "";

            if (style != null) {
                if (overrideTag != null) {
                    tag = style.withClass(overrideTag);
                    overrideTag = null;
                } else {
                    tag += style.withMode(mode);

                    if (withCallingClass) {
                        tag += style.withClass(getCallingClassName());
                    }
                }
            } else {
                pen.ink(WARN, "Diary", "A style needs to be set in order for tags to be functional!", throwable);
            }

            if (args.length > 0) {
                msg = String.format(msg, args);
            }

            pen.ink(mode, tag, msg, throwable);
        }

        /**
         * Manually sets the next log call string the specified tag.
         * This is a one-time operation, and only the subsequent log call will contain this tag.
         *
         * @param tag Tag
         * @return {@link Page}
         */
        public Page tag(String tag) {
            this.overrideTag = tag;

            return this;
        }

        /**
         * Writes an {@link #ASSERT} entry.
         *
         * @param msg  The message to log.
         * @param args Supplemented arguments to add into the log.
         */
        public void a(String msg, Object... args) {
            write(ASSERT, null, msg, args);
        }

        /**
         * Writes a {@link #DEBUG} entry.
         *
         * @param msg  The message to log.
         * @param args Supplemented arguments to add into the log.
         */
        public void d(String msg, Object... args) {
            write(DEBUG, null, msg, args);
        }

        /**
         * Writes an {@link #ERROR} entry.
         *
         * @param e    Exception to log.
         * @param msg  The message to log.
         * @param args Supplemented arguments to add into the log.
         */
        public void e(Throwable e, String msg, Object... args) {
            write(ERROR, e, msg, args);
        }

        /**
         * Writes an {@link #ERROR} entry.
         *
         * @param msg  The message to log.
         * @param args Supplemented arguments to add into the log.
         */
        public void e(String msg, Object... args) {
            write(ERROR, null, msg, args);
        }

        /**
         * Writes an {@link #INFO} entry.
         *
         * @param msg  The message to log.
         * @param args Supplemented arguments to add into the log.
         */
        public void i(String msg, Object... args) {
            write(INFO, null, msg, args);
        }

        /**
         * Writes a {@link #VERBOSE} entry.
         *
         * @param msg  The message to log.
         * @param args Supplemented arguments to add into the log.
         */
        public void v(String msg, Object... args) {
            write(VERBOSE, null, msg, args);
        }

        /**
         * Writes a {@link #WARN} entry.
         *
         * @param msg  The message to log.
         * @param args Supplemented arguments to add into the log.
         */
        public void w(String msg, Object... args) {
            write(WARN, null, msg, args);
        }

        /**
         * Page Builder
         */
        public static final class Builder {
            private Pen pen;
            private Style style;
            private boolean withCallingClass;
            private Evaluate evaluation;

            public Builder() {

            }

            /**
             * Sets a {@link Pen} for the Page to use.
             *
             * @param pen Pen requested.
             * @return Builder
             */
            public Builder setPen(Pen pen) {
                this.pen = pen;
                return this;
            }

            /**
             * Sets a {@link Style} for the Page to use.
             *
             * @param style Style requested.
             * @return Builder
             */
            public Builder setStyle(Style style) {
                this.style = style;
                return this;
            }

            /**
             * Determines if the tag should include inferred calling classes or not.
             *
             * @param withCallingClass Boolean
             * @return Builder
             */
            public Builder withCallingClass(boolean withCallingClass) {
                this.withCallingClass = withCallingClass;
                return this;
            }

            /**
             * Determines if the page should or should not log, dependent on an evaluation.
             *
             * @param evaluation {@link Evaluate}
             * @return Builder
             */
            public Builder withEvaluation(Evaluate evaluation) {
                this.evaluation = evaluation;
                return this;
            }

            /**
             * Builds and adds the Page to the Diary.
             *
             * @return Page
             */
            public Page addToDiary() {
                Page page = build();
                Diary.addPage(page);

                return page;
            }

            /**
             * Builds a Page.
             *
             * @return {@link Page}
             */
            public Page build() {
                if (style == null) style = noStyle;
                return new Page(pen, style, withCallingClass, evaluation);
            }
        }
    }

    /**
     * Interface responsible for determining if the page should log.
     */
    public interface Evaluate {
        /**
         * Determines if a page should log.
         *
         * @param mode Logging mode.
         * @return Boolean of result
         */
        boolean run(int mode);
    }

    /**
     * Interface responsible for choosing how to style a log message.
     */
    public interface Style {
        /**
         * Formats a message string a tag.
         *
         * @param mode Output mode.
         * @return Formatted output mode.
         */
        String withMode(int mode);

        /**
         * Formats a message string a class.
         *
         * @param className Class called from.
         * @return Formatted class name.
         */
        String withClass(String className);
    }

    /**
     * Interface responsible for choosing how to output log messages.
     */
    public interface Pen {
        /**
         * Writes out to whatever implemented console output one chooses.
         *
         * @param mode      Output mode.
         * @param tag       Message tag.
         * @param msg       The actual message.
         * @param throwable Throwable
         */
        void ink(int mode, String tag, String msg, Throwable throwable);
    }

    /**
     * A Pen for Android.
     * <p>
     * Implements the {@link Log} functionality.
     */
    public static class AndroidPen implements Pen {
        @Override
        public void ink(int mode, String tag, String msg, Throwable throwable) {
            if (throwable != null) {
                msg += "\n" + getStackTrace(throwable);
            }

            Log.println(mode, tag, msg);
        }

        private String getStackTrace(Throwable t) {
            // Taken out of the Android SDK.
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            t.printStackTrace(pw);
            pw.flush();

            return sw.toString();
        }
    }

    /**
     * A Style for Android
     * <p>
     * Since Android is using the AndroidPen, we don't style and let {@link Log} do it for us.
     */
    public static class AndroidStyle implements Style {
        @Override
        public String withMode(int mode) {
            return "";
        }

        @Override
        public String withClass(String className) {
            return className;
        }
    }

    /**
     * A Style for System.out
     * <p>
     * Makes it look nice and pretty!
     */
    public static class SystemOutStyle implements Style {
        @Override
        public String withMode(int mode) {
            switch (mode) {
                case Diary.ASSERT:
                    return "[ASSERT]";
                case Diary.DEBUG:
                    return "[DEBUG]";
                case Diary.ERROR:
                    return "[ERROR]";
                case Diary.INFO:
                    return "[INFO]";
                case Diary.VERBOSE:
                    return "[VERBOSE]";
                case Diary.WARN:
                    return "[WARN]";
                default:
                    return "";
            }
        }

        @Override
        public String withClass(String className) {
            return "[" + className + "] ";
        }
    }

    /**
     * A {@link Pen} that does nothing.
     * :'(
     */
    private static final Pen emptyPen = new Pen() {
        @Override
        public void ink(int mode, String tag, String msg, Throwable throwable) {

        }
    };

    /**
     * A {@link Style} that has no style.
     * :'(
     */
    private static final Style noStyle = new Style() {
        @Override
        public String withMode(int mode) {
            return "";
        }

        @Override
        public String withClass(String className) {
            return className;
        }
    };
}