/*
 *  The scanner definition for COOL.
 */
import java_cup.runtime.Symbol;


class CoolLexer implements java_cup.runtime.Scanner {
	private final int YY_BUFFER_SIZE = 512;
	private final int YY_F = -1;
	private final int YY_NO_STATE = -1;
	private final int YY_NOT_ACCEPT = 0;
	private final int YY_START = 1;
	private final int YY_END = 2;
	private final int YY_NO_ANCHOR = 4;
	private final int YY_BOL = 128;
	private final int YY_EOF = 129;

/*  Stuff enclosed in %{ %} is copied verbatim to the lexer class
 *  definition, all the extra variables/functions you want to use in the
 *  lexer actions should go here.  Don't remove or modify anything that
 *  was there initially.  */
    static int commentDepth = 0;
    String errorMessage = new String();
    // Max size of string constants
    static int MAX_STR_CONST = 1025;
    // For assembling string constants
    StringBuffer string_buf = new StringBuffer();
    private int curr_lineno = 1;
    int get_curr_lineno() {
    return curr_lineno;
    }
    private AbstractSymbol filename;
    void set_filename(String fname) {
    filename = AbstractTable.stringtable.addString(fname);
    }
    AbstractSymbol curr_filename() {
    return filename;
    }
	private java.io.BufferedReader yy_reader;
	private int yy_buffer_index;
	private int yy_buffer_read;
	private int yy_buffer_start;
	private int yy_buffer_end;
	private char yy_buffer[];
	private boolean yy_at_bol;
	private int yy_lexical_state;

	CoolLexer (java.io.Reader reader) {
		this ();
		if (null == reader) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(reader);
	}

	CoolLexer (java.io.InputStream instream) {
		this ();
		if (null == instream) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(new java.io.InputStreamReader(instream));
	}

	private CoolLexer () {
		yy_buffer = new char[YY_BUFFER_SIZE];
		yy_buffer_read = 0;
		yy_buffer_index = 0;
		yy_buffer_start = 0;
		yy_buffer_end = 0;
		yy_at_bol = true;
		yy_lexical_state = YYINITIAL;

/*  Stuff enclosed in %init{ %init} is copied verbatim to the lexer
 *  class constructor, all the extra initialization you want to do should
 *  go here.  Don't remove or modify anything that was there initially. */
	}

	private boolean yy_eof_done = false;
	private final int STRING = 3;
	private final int YYINITIAL = 0;
	private final int COMMENT = 1;
	private final int BREAKOUT = 4;
	private final int LINECOMMENT = 2;
	private final int yy_state_dtrans[] = {
		0,
		65,
		87,
		91,
		94
	};
	private void yybegin (int state) {
		yy_lexical_state = state;
	}
	private int yy_advance ()
		throws java.io.IOException {
		int next_read;
		int i;
		int j;

		if (yy_buffer_index < yy_buffer_read) {
			return yy_buffer[yy_buffer_index++];
		}

		if (0 != yy_buffer_start) {
			i = yy_buffer_start;
			j = 0;
			while (i < yy_buffer_read) {
				yy_buffer[j] = yy_buffer[i];
				++i;
				++j;
			}
			yy_buffer_end = yy_buffer_end - yy_buffer_start;
			yy_buffer_start = 0;
			yy_buffer_read = j;
			yy_buffer_index = j;
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}

		while (yy_buffer_index >= yy_buffer_read) {
			if (yy_buffer_index >= yy_buffer.length) {
				yy_buffer = yy_double(yy_buffer);
			}
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}
		return yy_buffer[yy_buffer_index++];
	}
	private void yy_move_end () {
		if (yy_buffer_end > yy_buffer_start &&
		    '\n' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
		if (yy_buffer_end > yy_buffer_start &&
		    '\r' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
	}
	private boolean yy_last_was_cr=false;
	private void yy_mark_start () {
		yy_buffer_start = yy_buffer_index;
	}
	private void yy_mark_end () {
		yy_buffer_end = yy_buffer_index;
	}
	private void yy_to_mark () {
		yy_buffer_index = yy_buffer_end;
		yy_at_bol = (yy_buffer_end > yy_buffer_start) &&
		            ('\r' == yy_buffer[yy_buffer_end-1] ||
		             '\n' == yy_buffer[yy_buffer_end-1] ||
		             2028/*LS*/ == yy_buffer[yy_buffer_end-1] ||
		             2029/*PS*/ == yy_buffer[yy_buffer_end-1]);
	}
	private java.lang.String yytext () {
		return (new java.lang.String(yy_buffer,
			yy_buffer_start,
			yy_buffer_end - yy_buffer_start));
	}
	private int yylength () {
		return yy_buffer_end - yy_buffer_start;
	}
	private char[] yy_double (char buf[]) {
		int i;
		char newbuf[];
		newbuf = new char[2*buf.length];
		for (i = 0; i < buf.length; ++i) {
			newbuf[i] = buf[i];
		}
		return newbuf;
	}
	private final int YY_E_INTERNAL = 0;
	private final int YY_E_MATCH = 1;
	private java.lang.String yy_error_string[] = {
		"Error: Internal error.\n",
		"Error: Unmatched input.\n"
	};
	private void yy_error (int code,boolean fatal) {
		java.lang.System.out.print(yy_error_string[code]);
		java.lang.System.out.flush();
		if (fatal) {
			throw new Error("Fatal Error.\n");
		}
	}
	private int[][] unpackFromString(int size1, int size2, String st) {
		int colonIndex = -1;
		String lengthString;
		int sequenceLength = 0;
		int sequenceInteger = 0;

		int commaIndex;
		String workString;

		int res[][] = new int[size1][size2];
		for (int i= 0; i < size1; i++) {
			for (int j= 0; j < size2; j++) {
				if (sequenceLength != 0) {
					res[i][j] = sequenceInteger;
					sequenceLength--;
					continue;
				}
				commaIndex = st.indexOf(',');
				workString = (commaIndex==-1) ? st :
					st.substring(0, commaIndex);
				st = st.substring(commaIndex+1);
				colonIndex = workString.indexOf(':');
				if (colonIndex == -1) {
					res[i][j]=Integer.parseInt(workString);
					continue;
				}
				lengthString =
					workString.substring(colonIndex+1);
				sequenceLength=Integer.parseInt(lengthString);
				workString=workString.substring(0,colonIndex);
				sequenceInteger=Integer.parseInt(workString);
				res[i][j] = sequenceInteger;
				sequenceLength--;
			}
		}
		return res;
	}
	private int yy_acpt[] = {
		/* 0 */ YY_NOT_ACCEPT,
		/* 1 */ YY_NO_ANCHOR,
		/* 2 */ YY_NO_ANCHOR,
		/* 3 */ YY_NO_ANCHOR,
		/* 4 */ YY_NO_ANCHOR,
		/* 5 */ YY_NO_ANCHOR,
		/* 6 */ YY_NO_ANCHOR,
		/* 7 */ YY_NO_ANCHOR,
		/* 8 */ YY_NO_ANCHOR,
		/* 9 */ YY_NO_ANCHOR,
		/* 10 */ YY_NO_ANCHOR,
		/* 11 */ YY_NO_ANCHOR,
		/* 12 */ YY_NO_ANCHOR,
		/* 13 */ YY_NO_ANCHOR,
		/* 14 */ YY_NO_ANCHOR,
		/* 15 */ YY_NO_ANCHOR,
		/* 16 */ YY_NO_ANCHOR,
		/* 17 */ YY_NO_ANCHOR,
		/* 18 */ YY_NO_ANCHOR,
		/* 19 */ YY_NO_ANCHOR,
		/* 20 */ YY_NO_ANCHOR,
		/* 21 */ YY_NO_ANCHOR,
		/* 22 */ YY_NO_ANCHOR,
		/* 23 */ YY_NO_ANCHOR,
		/* 24 */ YY_NO_ANCHOR,
		/* 25 */ YY_NO_ANCHOR,
		/* 26 */ YY_NO_ANCHOR,
		/* 27 */ YY_NO_ANCHOR,
		/* 28 */ YY_NO_ANCHOR,
		/* 29 */ YY_NO_ANCHOR,
		/* 30 */ YY_NO_ANCHOR,
		/* 31 */ YY_NO_ANCHOR,
		/* 32 */ YY_NO_ANCHOR,
		/* 33 */ YY_NO_ANCHOR,
		/* 34 */ YY_NO_ANCHOR,
		/* 35 */ YY_NO_ANCHOR,
		/* 36 */ YY_NO_ANCHOR,
		/* 37 */ YY_NO_ANCHOR,
		/* 38 */ YY_NO_ANCHOR,
		/* 39 */ YY_NO_ANCHOR,
		/* 40 */ YY_NO_ANCHOR,
		/* 41 */ YY_NO_ANCHOR,
		/* 42 */ YY_NO_ANCHOR,
		/* 43 */ YY_NO_ANCHOR,
		/* 44 */ YY_NO_ANCHOR,
		/* 45 */ YY_NO_ANCHOR,
		/* 46 */ YY_NO_ANCHOR,
		/* 47 */ YY_NO_ANCHOR,
		/* 48 */ YY_NO_ANCHOR,
		/* 49 */ YY_NO_ANCHOR,
		/* 50 */ YY_NO_ANCHOR,
		/* 51 */ YY_NO_ANCHOR,
		/* 52 */ YY_NO_ANCHOR,
		/* 53 */ YY_NO_ANCHOR,
		/* 54 */ YY_NO_ANCHOR,
		/* 55 */ YY_NO_ANCHOR,
		/* 56 */ YY_NO_ANCHOR,
		/* 57 */ YY_NO_ANCHOR,
		/* 58 */ YY_NO_ANCHOR,
		/* 59 */ YY_NO_ANCHOR,
		/* 60 */ YY_NO_ANCHOR,
		/* 61 */ YY_NO_ANCHOR,
		/* 62 */ YY_NO_ANCHOR,
		/* 63 */ YY_NO_ANCHOR,
		/* 64 */ YY_NO_ANCHOR,
		/* 65 */ YY_NOT_ACCEPT,
		/* 66 */ YY_NO_ANCHOR,
		/* 67 */ YY_NO_ANCHOR,
		/* 68 */ YY_NO_ANCHOR,
		/* 69 */ YY_NO_ANCHOR,
		/* 70 */ YY_NO_ANCHOR,
		/* 71 */ YY_NO_ANCHOR,
		/* 72 */ YY_NO_ANCHOR,
		/* 73 */ YY_NO_ANCHOR,
		/* 74 */ YY_NO_ANCHOR,
		/* 75 */ YY_NO_ANCHOR,
		/* 76 */ YY_NO_ANCHOR,
		/* 77 */ YY_NO_ANCHOR,
		/* 78 */ YY_NO_ANCHOR,
		/* 79 */ YY_NO_ANCHOR,
		/* 80 */ YY_NO_ANCHOR,
		/* 81 */ YY_NO_ANCHOR,
		/* 82 */ YY_NO_ANCHOR,
		/* 83 */ YY_NO_ANCHOR,
		/* 84 */ YY_NO_ANCHOR,
		/* 85 */ YY_NO_ANCHOR,
		/* 86 */ YY_NO_ANCHOR,
		/* 87 */ YY_NOT_ACCEPT,
		/* 88 */ YY_NO_ANCHOR,
		/* 89 */ YY_NO_ANCHOR,
		/* 90 */ YY_NO_ANCHOR,
		/* 91 */ YY_NOT_ACCEPT,
		/* 92 */ YY_NO_ANCHOR,
		/* 93 */ YY_NO_ANCHOR,
		/* 94 */ YY_NOT_ACCEPT,
		/* 95 */ YY_NO_ANCHOR,
		/* 96 */ YY_NO_ANCHOR,
		/* 97 */ YY_NO_ANCHOR,
		/* 98 */ YY_NO_ANCHOR,
		/* 99 */ YY_NO_ANCHOR,
		/* 100 */ YY_NO_ANCHOR,
		/* 101 */ YY_NO_ANCHOR,
		/* 102 */ YY_NO_ANCHOR,
		/* 103 */ YY_NO_ANCHOR,
		/* 104 */ YY_NO_ANCHOR,
		/* 105 */ YY_NO_ANCHOR,
		/* 106 */ YY_NO_ANCHOR,
		/* 107 */ YY_NO_ANCHOR,
		/* 108 */ YY_NO_ANCHOR,
		/* 109 */ YY_NO_ANCHOR,
		/* 110 */ YY_NO_ANCHOR,
		/* 111 */ YY_NO_ANCHOR,
		/* 112 */ YY_NO_ANCHOR,
		/* 113 */ YY_NO_ANCHOR,
		/* 114 */ YY_NO_ANCHOR,
		/* 115 */ YY_NO_ANCHOR,
		/* 116 */ YY_NO_ANCHOR,
		/* 117 */ YY_NO_ANCHOR,
		/* 118 */ YY_NO_ANCHOR,
		/* 119 */ YY_NO_ANCHOR,
		/* 120 */ YY_NO_ANCHOR,
		/* 121 */ YY_NO_ANCHOR,
		/* 122 */ YY_NO_ANCHOR,
		/* 123 */ YY_NO_ANCHOR,
		/* 124 */ YY_NO_ANCHOR,
		/* 125 */ YY_NO_ANCHOR,
		/* 126 */ YY_NO_ANCHOR,
		/* 127 */ YY_NO_ANCHOR,
		/* 128 */ YY_NO_ANCHOR,
		/* 129 */ YY_NO_ANCHOR,
		/* 130 */ YY_NO_ANCHOR,
		/* 131 */ YY_NO_ANCHOR,
		/* 132 */ YY_NO_ANCHOR,
		/* 133 */ YY_NO_ANCHOR,
		/* 134 */ YY_NO_ANCHOR,
		/* 135 */ YY_NO_ANCHOR,
		/* 136 */ YY_NO_ANCHOR,
		/* 137 */ YY_NO_ANCHOR,
		/* 138 */ YY_NO_ANCHOR,
		/* 139 */ YY_NO_ANCHOR,
		/* 140 */ YY_NO_ANCHOR,
		/* 141 */ YY_NO_ANCHOR,
		/* 142 */ YY_NO_ANCHOR,
		/* 143 */ YY_NO_ANCHOR,
		/* 144 */ YY_NO_ANCHOR,
		/* 145 */ YY_NO_ANCHOR,
		/* 146 */ YY_NO_ANCHOR,
		/* 147 */ YY_NO_ANCHOR,
		/* 148 */ YY_NO_ANCHOR,
		/* 149 */ YY_NO_ANCHOR,
		/* 150 */ YY_NO_ANCHOR,
		/* 151 */ YY_NO_ANCHOR,
		/* 152 */ YY_NO_ANCHOR,
		/* 153 */ YY_NO_ANCHOR,
		/* 154 */ YY_NO_ANCHOR,
		/* 155 */ YY_NO_ANCHOR,
		/* 156 */ YY_NO_ANCHOR,
		/* 157 */ YY_NO_ANCHOR,
		/* 158 */ YY_NO_ANCHOR,
		/* 159 */ YY_NO_ANCHOR,
		/* 160 */ YY_NO_ANCHOR,
		/* 161 */ YY_NO_ANCHOR,
		/* 162 */ YY_NO_ANCHOR,
		/* 163 */ YY_NO_ANCHOR,
		/* 164 */ YY_NO_ANCHOR,
		/* 165 */ YY_NO_ANCHOR,
		/* 166 */ YY_NO_ANCHOR,
		/* 167 */ YY_NO_ANCHOR,
		/* 168 */ YY_NO_ANCHOR,
		/* 169 */ YY_NO_ANCHOR,
		/* 170 */ YY_NO_ANCHOR,
		/* 171 */ YY_NO_ANCHOR,
		/* 172 */ YY_NO_ANCHOR,
		/* 173 */ YY_NO_ANCHOR
	};
	private int yy_cmap[] = unpackFromString(1,130,
"56:9,62,53,62:2,57,56:18,62,56,52,56:5,4,7,3,10,8,6,12,9,58:10,13,5,11,1,2," +
"56,17,23,59,19,47,32,34,59,40,36,59:2,21,59,38,45,49,59,28,25,41,30,43,51,5" +
"9:3,56,54,56:2,60,56,22,55,18,46,31,33,61,39,35,61:2,20,61,37,44,48,61,27,2" +
"4,26,29,42,50,61:3,14,56,15,16,56,0:2")[0];

	private int yy_rmap[] = unpackFromString(1,174,
"0,1,2,1,3,4,1,5,1:4,6,1:6,7,8,1:3,9,1:6,10:2,11,10:16,1:15,12,13,14,15:2,16" +
",15:14,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39" +
",40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64" +
",65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89" +
",90,91,92,93,10,15,94,95,96,97,98,99,100,101,102,103")[0];

	private int yy_nxt[][] = unpackFromString(104,63,
"1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,123,124,162,163,162,163," +
"164,162,163,162,163,166,165,66,67,88,89,125,126,162,163,167,162,163,92,93,1" +
"62,163,168,169,170,171,21,22,3,162,3,23,24,163,3,162,23,-1:65,25,-1:67,26,-" +
"1:58,27,-1:65,28,-1:57,29,-1:4,30,-1:74,162:2,172:2,127:2,162:28,-1:3,162,-" +
"1:2,162:4,-1:19,163:2,173:2,128:2,163:28,-1:3,163,-1:2,163:4,-1:59,24,-1:22" +
",162:34,-1:3,162,-1:2,162:4,-1:19,162:21,153:2,162:11,-1:3,162,-1:2,162:4,-" +
"1,1,50:2,85,90,50:48,51,50:9,-1:18,162:4,139:2,162:11,31:2,162:15,-1:3,162," +
"-1:2,162:4,-1:19,163:17,68:2,163:15,-1:3,163,-1:2,163:4,-1:19,163:34,-1:3,1" +
"63,-1:2,163:4,-1:19,163:21,148:2,163:11,-1:3,163,-1:2,163:4,-1:8,52,-1:56,5" +
"9:25,60,59:6,61,59:3,62,59:15,63,59,64,59,-1,59:5,1,54:52,55,54:3,-1,54:5,-" +
"1:18,162:6,141:2,162:7,32:2,162:2,33:2,162:13,-1:3,162,-1:2,162:4,-1:19,163" +
":6,136:2,163:7,69:2,163:2,70:2,163:13,-1:3,163,-1:2,163:4,-1:4,53,-1:59,1,5" +
"6:51,57,58,86,56:8,-1:18,162:15,34:2,162:17,-1:3,162,-1:2,162:4,-1:19,163:1" +
"5,71:2,163:17,-1:3,163,-1:2,163:4,-1,1,3:52,-1,3:3,-1,3:5,-1:18,162:8,35,16" +
"2:14,35,162:10,-1:3,162,-1:2,162:4,-1:19,163:8,72,163:14,72,163:10,-1:3,163" +
",-1:2,163:4,-1:19,162:32,36:2,-1:3,162,-1:2,162:4,-1:19,163:32,73:2,-1:3,16" +
"3,-1:2,163:4,-1:19,162:8,37,162:14,37,162:10,-1:3,162,-1:2,162:4,-1:19,163:" +
"8,74,163:14,74,163:10,-1:3,163,-1:2,163:4,-1:19,162:13,38:2,162:19,-1:3,162" +
",-1:2,162:4,-1:19,163:13,75:2,163:19,-1:3,163,-1:2,163:4,-1:19,162:30,39:2," +
"162:2,-1:3,162,-1:2,162:4,-1:19,163:30,76:2,163:2,-1:3,163,-1:2,163:4,-1:19" +
",162:13,40:2,162:19,-1:3,162,-1:2,162:4,-1:19,163:13,78:2,163:19,-1:3,163,-" +
"1:2,163:4,-1:19,162:19,41:2,162:13,-1:3,162,-1:2,162:4,-1:19,79:2,163:32,-1" +
":3,163,-1:2,163:4,-1:19,162:13,42:2,162:19,-1:3,162,-1:2,162:4,-1:19,163:19" +
",77:2,163:13,-1:3,163,-1:2,163:4,-1:19,43:2,162:32,-1:3,162,-1:2,162:4,-1:1" +
"9,163:2,80:2,163:30,-1:3,163,-1:2,163:4,-1:19,162:2,44:2,162:30,-1:3,162,-1" +
":2,162:4,-1:19,163:6,81:2,163:26,-1:3,163,-1:2,163:4,-1:19,162:6,45:2,162:2" +
"6,-1:3,162,-1:2,162:4,-1:19,163:13,82:2,163:19,-1:3,163,-1:2,163:4,-1:19,16" +
"2:13,46:2,162:19,-1:3,162,-1:2,162:4,-1:19,163:28,83:2,163:4,-1:3,163,-1:2," +
"163:4,-1:19,162:13,47:2,162:19,-1:3,162,-1:2,162:4,-1:19,163:6,84:2,163:26," +
"-1:3,163,-1:2,163:4,-1:19,162:28,48:2,162:4,-1:3,162,-1:2,162:4,-1:19,162:6" +
",49:2,162:26,-1:3,162,-1:2,162:4,-1:19,162:13,95:2,162:11,129:2,162:6,-1:3," +
"162,-1:2,162:4,-1:19,163:13,96:2,163:11,130:2,163:6,-1:3,163,-1:2,163:4,-1:" +
"19,162:13,97:2,162:11,99:2,162:6,-1:3,162,-1:2,162:4,-1:19,163:13,98:2,163:" +
"11,100:2,163:6,-1:3,163,-1:2,163:4,-1:19,162:6,101:2,162:26,-1:3,162,-1:2,1" +
"62:4,-1:19,163:6,102:2,163:26,-1:3,163,-1:2,163:4,-1:19,162:26,103:2,162:6," +
"-1:3,162,-1:2,162:4,-1:19,163:26,104:2,163:6,-1:3,163,-1:2,163:4,-1:19,162:" +
"11,105:2,162:21,-1:3,162,-1:2,162:4,-1:19,163:6,106:2,163:26,-1:3,163,-1:2," +
"163:4,-1:19,162:13,107:2,162:19,-1:3,162,-1:2,162:4,-1:19,163:4,108:2,163:2" +
"8,-1:3,163,-1:2,163:4,-1:19,162:6,109:2,162:26,-1:3,162,-1:2,162:4,-1:19,16" +
"3:24,146:2,163:8,-1:3,163,-1:2,163:4,-1:19,162:4,111:2,162:28,-1:3,162,-1:2" +
",162:4,-1:19,163:13,110:2,163:19,-1:3,163,-1:2,163:4,-1:19,162:2,149:2,162:" +
"30,-1:3,162,-1:2,162:4,-1:19,163:26,112:2,163:6,-1:3,163,-1:2,163:4,-1:19,1" +
"62:24,151:2,162:8,-1:3,162,-1:2,162:4,-1:19,163:17,150:2,163:15,-1:3,163,-1" +
":2,163:4,-1:19,162:26,113:2,162:6,-1:3,162,-1:2,162:4,-1:19,163:6,114:2,163" +
":26,-1:3,163,-1:2,163:4,-1:19,162:17,155:2,162:15,-1:3,162,-1:2,162:4,-1:19" +
",163:26,152:2,163:6,-1:3,163,-1:2,163:4,-1:19,162:6,115:2,162:26,-1:3,162,-" +
"1:2,162:4,-1:19,163:13,154:2,163:19,-1:3,163,-1:2,163:4,-1:19,162:6,117:2,1" +
"62:26,-1:3,162,-1:2,162:4,-1:19,163:2,116:2,163:30,-1:3,163,-1:2,163:4,-1:1" +
"9,162:26,157:2,162:6,-1:3,162,-1:2,162:4,-1:19,163:17,118:2,163:15,-1:3,163" +
",-1:2,163:4,-1:19,162:13,159:2,162:19,-1:3,162,-1:2,162:4,-1:19,163:9,156:2" +
",163:23,-1:3,163,-1:2,163:4,-1:19,162:2,119:2,162:30,-1:3,162,-1:2,162:4,-1" +
":19,163:17,158:2,163:15,-1:3,163,-1:2,163:4,-1:19,162:17,121:2,162:15,-1:3," +
"162,-1:2,162:4,-1:19,163:8,120,163:14,120,163:10,-1:3,163,-1:2,163:4,-1:19," +
"162:9,160:2,162:23,-1:3,162,-1:2,162:4,-1:19,162:17,161:2,162:15,-1:3,162,-" +
"1:2,162:4,-1:19,162:8,122,162:14,122,162:10,-1:3,162,-1:2,162:4,-1:19,162:9" +
",131:2,162:10,133:2,162:11,-1:3,162,-1:2,162:4,-1:19,163:2,132:2,163:2,134:" +
"2,163:26,-1:3,163,-1:2,163:4,-1:19,162:2,135:2,162:2,137:2,162:26,-1:3,162," +
"-1:2,162:4,-1:19,163:21,138:2,163:11,-1:3,163,-1:2,163:4,-1:19,162:26,143:2" +
",162:6,-1:3,162,-1:2,162:4,-1:19,163:26,140:2,163:6,-1:3,163,-1:2,163:4,-1:" +
"19,162:21,145:2,162:11,-1:3,162,-1:2,162:4,-1:19,163:21,142:2,163:11,-1:3,1" +
"63,-1:2,163:4,-1:19,162:4,147:2,162:28,-1:3,162,-1:2,162:4,-1:19,163:4,144:" +
"2,163:28,-1:3,163,-1:2,163:4,-1");

	public java_cup.runtime.Symbol next_token ()
		throws java.io.IOException {
		int yy_lookahead;
		int yy_anchor = YY_NO_ANCHOR;
		int yy_state = yy_state_dtrans[yy_lexical_state];
		int yy_next_state = YY_NO_STATE;
		int yy_last_accept_state = YY_NO_STATE;
		boolean yy_initial = true;
		int yy_this_accept;

		yy_mark_start();
		yy_this_accept = yy_acpt[yy_state];
		if (YY_NOT_ACCEPT != yy_this_accept) {
			yy_last_accept_state = yy_state;
			yy_mark_end();
		}
		while (true) {
			if (yy_initial && yy_at_bol) yy_lookahead = YY_BOL;
			else yy_lookahead = yy_advance();
			yy_next_state = YY_F;
			yy_next_state = yy_nxt[yy_rmap[yy_state]][yy_cmap[yy_lookahead]];
			if (YY_EOF == yy_lookahead && true == yy_initial) {

/*  Stuff enclosed in %eofval{ %eofval} specifies java code that is
 *  executed when end-of-file is reached.  If you use multiple lexical
 *  states and want to do something special if an EOF is encountered in
 *  one of those states, place your code in the switch statement.
 *  Ultimately, you should return the EOF symbol, or your lexer won't
 *  work.  */
    switch(yy_lexical_state) {
    case YYINITIAL:
    /* nothing special to do in the initial state */
    break;
    case COMMENT:
        yybegin(YYINITIAL);
        errorMessage = new String("EOF in comment");
        return new Symbol(TokenConstants.ERROR, new StringSymbol(errorMessage, errorMessage.length(), 0));
    case STRING:
        yybegin(YYINITIAL);
        errorMessage = new String("EOF in string");
        return new Symbol(TokenConstants.ERROR, new StringSymbol(errorMessage, errorMessage.length(), 0));
    }
    return new Symbol(TokenConstants.EOF);
			}
			if (YY_F != yy_next_state) {
				yy_state = yy_next_state;
				yy_initial = false;
				yy_this_accept = yy_acpt[yy_state];
				if (YY_NOT_ACCEPT != yy_this_accept) {
					yy_last_accept_state = yy_state;
					yy_mark_end();
				}
			}
			else {
				if (YY_NO_STATE == yy_last_accept_state) {
					throw (new Error("Lexical Error: Unmatched Input."));
				}
				else {
					yy_anchor = yy_acpt[yy_last_accept_state];
					if (0 != (YY_END & yy_anchor)) {
						yy_move_end();
					}
					yy_to_mark();
					switch (yy_last_accept_state) {
					case 1:
						
					case -2:
						break;
					case 2:
						{return new Symbol(TokenConstants.EQ);}
					case -3:
						break;
					case 3:
						{return new Symbol(TokenConstants.ERROR, new StringSymbol(yytext(), yytext().length(), 0));}
					case -4:
						break;
					case 4:
						{return new Symbol(TokenConstants.MULT);}
					case -5:
						break;
					case 5:
						{return new Symbol(TokenConstants.LPAREN);}
					case -6:
						break;
					case 6:
						{return new Symbol(TokenConstants.SEMI);}
					case -7:
						break;
					case 7:
						{return new Symbol(TokenConstants.MINUS);}
					case -8:
						break;
					case 8:
						{return new Symbol(TokenConstants.RPAREN);}
					case -9:
						break;
					case 9:
						{return new Symbol(TokenConstants.COMMA);}
					case -10:
						break;
					case 10:
						{return new Symbol(TokenConstants.DIV);}
					case -11:
						break;
					case 11:
						{return new Symbol(TokenConstants.PLUS);}
					case -12:
						break;
					case 12:
						{return new Symbol(TokenConstants.LT);}
					case -13:
						break;
					case 13:
						{return new Symbol(TokenConstants.DOT);}
					case -14:
						break;
					case 14:
						{return new Symbol(TokenConstants.COLON);}
					case -15:
						break;
					case 15:
						{return new Symbol(TokenConstants.LBRACE);}
					case -16:
						break;
					case 16:
						{return new Symbol(TokenConstants.RBRACE);}
					case -17:
						break;
					case 17:
						{return new Symbol(TokenConstants.NEG);}
					case -18:
						break;
					case 18:
						{return new Symbol(TokenConstants.AT);}
					case -19:
						break;
					case 19:
						{return new Symbol(TokenConstants.OBJECTID, new StringSymbol(yytext(), yytext().length(), 0));}
					case -20:
						break;
					case 20:
						{return new Symbol(TokenConstants.TYPEID, new StringSymbol(yytext(), yytext().length(), 0));}
					case -21:
						break;
					case 21:
						{yybegin(STRING); string_buf = new StringBuffer();}
					case -22:
						break;
					case 22:
						{curr_lineno++;}
					case -23:
						break;
					case 23:
						{;}
					case -24:
						break;
					case 24:
						{return new Symbol(TokenConstants.INT_CONST, new StringSymbol(yytext(), yytext().length(), 0));}
					case -25:
						break;
					case 25:
						{return new Symbol(TokenConstants.DARROW);}
					case -26:
						break;
					case 26:
						{return new Symbol(TokenConstants.ERROR, new StringSymbol(new String("Unmatched *)"), 12, 0));}
					case -27:
						break;
					case 27:
						{yybegin(COMMENT);}
					case -28:
						break;
					case 28:
						{yybegin(LINECOMMENT);}
					case -29:
						break;
					case 29:
						{return new Symbol(TokenConstants.LE);}
					case -30:
						break;
					case 30:
						{return new Symbol(TokenConstants.ASSIGN);}
					case -31:
						break;
					case 31:
						{return new Symbol(TokenConstants.FI);}
					case -32:
						break;
					case 32:
						{return new Symbol(TokenConstants.IF);}
					case -33:
						break;
					case 33:
						{return new Symbol(TokenConstants.IN);}
					case -34:
						break;
					case 34:
						{return new Symbol(TokenConstants.OF);}
					case -35:
						break;
					case 35:
						{return new Symbol(TokenConstants.LET);}
					case -36:
						break;
					case 36:
						{return new Symbol(TokenConstants.NEW);}
					case -37:
						break;
					case 37:
						{return new Symbol(TokenConstants.NOT);}
					case -38:
						break;
					case 38:
						{return new Symbol(TokenConstants.CASE);}
					case -39:
						break;
					case 39:
						{return new Symbol(TokenConstants.LOOP);}
					case -40:
						break;
					case 40:
						{return new Symbol(TokenConstants.BOOL_CONST, new Boolean(true));}
					case -41:
						break;
					case 41:
						{return new Symbol(TokenConstants.THEN);}
					case -42:
						break;
					case 42:
						{return new Symbol(TokenConstants.ELSE);}
					case -43:
						break;
					case 43:
						{return new Symbol(TokenConstants.ESAC);}
					case -44:
						break;
					case 44:
						{return new Symbol(TokenConstants.POOL);}
					case -45:
						break;
					case 45:
						{return new Symbol(TokenConstants.CLASS);}
					case -46:
						break;
					case 46:
						{return new Symbol(TokenConstants.BOOL_CONST, new Boolean(false));}
					case -47:
						break;
					case 47:
						{return new Symbol(TokenConstants.WHILE);}
					case -48:
						break;
					case 48:
						{return new Symbol(TokenConstants.ISVOID);}
					case -49:
						break;
					case 49:
						{return new Symbol(TokenConstants.INHERITS);}
					case -50:
						break;
					case 50:
						{;}
					case -51:
						break;
					case 51:
						{curr_lineno++;}
					case -52:
						break;
					case 52:
						{if(commentDepth > 0) {commentDepth--;} else {yybegin(YYINITIAL);}}
					case -53:
						break;
					case 53:
						{commentDepth++;}
					case -54:
						break;
					case 54:
						{;}
					case -55:
						break;
					case 55:
						{yybegin(YYINITIAL);curr_lineno++;}
					case -56:
						break;
					case 56:
						{string_buf.append(yytext());}
					case -57:
						break;
					case 57:
						{yybegin(YYINITIAL);
                                    String currentString = string_buf.toString();
                                    if (string_buf.length() >= MAX_STR_CONST) {
                                        errorMessage = new String("String constant too long");
                                        return new Symbol(TokenConstants.ERROR, new StringSymbol(errorMessage, errorMessage.length(), 0));}
                                    if (currentString.indexOf('\0') != -1) {
                                        errorMessage = new String("String contains null character");
                                        return new Symbol(TokenConstants.ERROR, new StringSymbol(errorMessage, errorMessage.length(), 0));}
                                    else {
                                        return new Symbol(TokenConstants.STR_CONST, new StringSymbol(currentString, currentString.length(), 0));}}
					case -58:
						break;
					case 58:
						{yybegin(YYINITIAL);
                                    curr_lineno++;
                                    errorMessage = new String("Unterminated string constant");
                                    return new Symbol(TokenConstants.ERROR, new StringSymbol(errorMessage, errorMessage.length(), 0));}
					case -59:
						break;
					case 59:
						{string_buf.append(yytext().substring(1));}
					case -60:
						break;
					case 60:
						{string_buf.append('\t');}
					case -61:
						break;
					case 61:
						{string_buf.append('\f');}
					case -62:
						break;
					case 62:
						{string_buf.append('\n');}
					case -63:
						break;
					case 63:
						{string_buf.append('\n');}
					case -64:
						break;
					case 64:
						{string_buf.append('\b');}
					case -65:
						break;
					case 66:
						{return new Symbol(TokenConstants.OBJECTID, new StringSymbol(yytext(), yytext().length(), 0));}
					case -66:
						break;
					case 67:
						{return new Symbol(TokenConstants.TYPEID, new StringSymbol(yytext(), yytext().length(), 0));}
					case -67:
						break;
					case 68:
						{return new Symbol(TokenConstants.FI);}
					case -68:
						break;
					case 69:
						{return new Symbol(TokenConstants.IF);}
					case -69:
						break;
					case 70:
						{return new Symbol(TokenConstants.IN);}
					case -70:
						break;
					case 71:
						{return new Symbol(TokenConstants.OF);}
					case -71:
						break;
					case 72:
						{return new Symbol(TokenConstants.LET);}
					case -72:
						break;
					case 73:
						{return new Symbol(TokenConstants.NEW);}
					case -73:
						break;
					case 74:
						{return new Symbol(TokenConstants.NOT);}
					case -74:
						break;
					case 75:
						{return new Symbol(TokenConstants.CASE);}
					case -75:
						break;
					case 76:
						{return new Symbol(TokenConstants.LOOP);}
					case -76:
						break;
					case 77:
						{return new Symbol(TokenConstants.THEN);}
					case -77:
						break;
					case 78:
						{return new Symbol(TokenConstants.ELSE);}
					case -78:
						break;
					case 79:
						{return new Symbol(TokenConstants.ESAC);}
					case -79:
						break;
					case 80:
						{return new Symbol(TokenConstants.POOL);}
					case -80:
						break;
					case 81:
						{return new Symbol(TokenConstants.CLASS);}
					case -81:
						break;
					case 82:
						{return new Symbol(TokenConstants.WHILE);}
					case -82:
						break;
					case 83:
						{return new Symbol(TokenConstants.ISVOID);}
					case -83:
						break;
					case 84:
						{return new Symbol(TokenConstants.INHERITS);}
					case -84:
						break;
					case 85:
						{;}
					case -85:
						break;
					case 86:
						{string_buf.append(yytext());}
					case -86:
						break;
					case 88:
						{return new Symbol(TokenConstants.OBJECTID, new StringSymbol(yytext(), yytext().length(), 0));}
					case -87:
						break;
					case 89:
						{return new Symbol(TokenConstants.TYPEID, new StringSymbol(yytext(), yytext().length(), 0));}
					case -88:
						break;
					case 90:
						{;}
					case -89:
						break;
					case 92:
						{return new Symbol(TokenConstants.OBJECTID, new StringSymbol(yytext(), yytext().length(), 0));}
					case -90:
						break;
					case 93:
						{return new Symbol(TokenConstants.TYPEID, new StringSymbol(yytext(), yytext().length(), 0));}
					case -91:
						break;
					case 95:
						{return new Symbol(TokenConstants.OBJECTID, new StringSymbol(yytext(), yytext().length(), 0));}
					case -92:
						break;
					case 96:
						{return new Symbol(TokenConstants.TYPEID, new StringSymbol(yytext(), yytext().length(), 0));}
					case -93:
						break;
					case 97:
						{return new Symbol(TokenConstants.OBJECTID, new StringSymbol(yytext(), yytext().length(), 0));}
					case -94:
						break;
					case 98:
						{return new Symbol(TokenConstants.TYPEID, new StringSymbol(yytext(), yytext().length(), 0));}
					case -95:
						break;
					case 99:
						{return new Symbol(TokenConstants.OBJECTID, new StringSymbol(yytext(), yytext().length(), 0));}
					case -96:
						break;
					case 100:
						{return new Symbol(TokenConstants.TYPEID, new StringSymbol(yytext(), yytext().length(), 0));}
					case -97:
						break;
					case 101:
						{return new Symbol(TokenConstants.OBJECTID, new StringSymbol(yytext(), yytext().length(), 0));}
					case -98:
						break;
					case 102:
						{return new Symbol(TokenConstants.TYPEID, new StringSymbol(yytext(), yytext().length(), 0));}
					case -99:
						break;
					case 103:
						{return new Symbol(TokenConstants.OBJECTID, new StringSymbol(yytext(), yytext().length(), 0));}
					case -100:
						break;
					case 104:
						{return new Symbol(TokenConstants.TYPEID, new StringSymbol(yytext(), yytext().length(), 0));}
					case -101:
						break;
					case 105:
						{return new Symbol(TokenConstants.OBJECTID, new StringSymbol(yytext(), yytext().length(), 0));}
					case -102:
						break;
					case 106:
						{return new Symbol(TokenConstants.TYPEID, new StringSymbol(yytext(), yytext().length(), 0));}
					case -103:
						break;
					case 107:
						{return new Symbol(TokenConstants.OBJECTID, new StringSymbol(yytext(), yytext().length(), 0));}
					case -104:
						break;
					case 108:
						{return new Symbol(TokenConstants.TYPEID, new StringSymbol(yytext(), yytext().length(), 0));}
					case -105:
						break;
					case 109:
						{return new Symbol(TokenConstants.OBJECTID, new StringSymbol(yytext(), yytext().length(), 0));}
					case -106:
						break;
					case 110:
						{return new Symbol(TokenConstants.TYPEID, new StringSymbol(yytext(), yytext().length(), 0));}
					case -107:
						break;
					case 111:
						{return new Symbol(TokenConstants.OBJECTID, new StringSymbol(yytext(), yytext().length(), 0));}
					case -108:
						break;
					case 112:
						{return new Symbol(TokenConstants.TYPEID, new StringSymbol(yytext(), yytext().length(), 0));}
					case -109:
						break;
					case 113:
						{return new Symbol(TokenConstants.OBJECTID, new StringSymbol(yytext(), yytext().length(), 0));}
					case -110:
						break;
					case 114:
						{return new Symbol(TokenConstants.TYPEID, new StringSymbol(yytext(), yytext().length(), 0));}
					case -111:
						break;
					case 115:
						{return new Symbol(TokenConstants.OBJECTID, new StringSymbol(yytext(), yytext().length(), 0));}
					case -112:
						break;
					case 116:
						{return new Symbol(TokenConstants.TYPEID, new StringSymbol(yytext(), yytext().length(), 0));}
					case -113:
						break;
					case 117:
						{return new Symbol(TokenConstants.OBJECTID, new StringSymbol(yytext(), yytext().length(), 0));}
					case -114:
						break;
					case 118:
						{return new Symbol(TokenConstants.TYPEID, new StringSymbol(yytext(), yytext().length(), 0));}
					case -115:
						break;
					case 119:
						{return new Symbol(TokenConstants.OBJECTID, new StringSymbol(yytext(), yytext().length(), 0));}
					case -116:
						break;
					case 120:
						{return new Symbol(TokenConstants.TYPEID, new StringSymbol(yytext(), yytext().length(), 0));}
					case -117:
						break;
					case 121:
						{return new Symbol(TokenConstants.OBJECTID, new StringSymbol(yytext(), yytext().length(), 0));}
					case -118:
						break;
					case 122:
						{return new Symbol(TokenConstants.OBJECTID, new StringSymbol(yytext(), yytext().length(), 0));}
					case -119:
						break;
					case 123:
						{return new Symbol(TokenConstants.OBJECTID, new StringSymbol(yytext(), yytext().length(), 0));}
					case -120:
						break;
					case 124:
						{return new Symbol(TokenConstants.TYPEID, new StringSymbol(yytext(), yytext().length(), 0));}
					case -121:
						break;
					case 125:
						{return new Symbol(TokenConstants.OBJECTID, new StringSymbol(yytext(), yytext().length(), 0));}
					case -122:
						break;
					case 126:
						{return new Symbol(TokenConstants.TYPEID, new StringSymbol(yytext(), yytext().length(), 0));}
					case -123:
						break;
					case 127:
						{return new Symbol(TokenConstants.OBJECTID, new StringSymbol(yytext(), yytext().length(), 0));}
					case -124:
						break;
					case 128:
						{return new Symbol(TokenConstants.TYPEID, new StringSymbol(yytext(), yytext().length(), 0));}
					case -125:
						break;
					case 129:
						{return new Symbol(TokenConstants.OBJECTID, new StringSymbol(yytext(), yytext().length(), 0));}
					case -126:
						break;
					case 130:
						{return new Symbol(TokenConstants.TYPEID, new StringSymbol(yytext(), yytext().length(), 0));}
					case -127:
						break;
					case 131:
						{return new Symbol(TokenConstants.OBJECTID, new StringSymbol(yytext(), yytext().length(), 0));}
					case -128:
						break;
					case 132:
						{return new Symbol(TokenConstants.TYPEID, new StringSymbol(yytext(), yytext().length(), 0));}
					case -129:
						break;
					case 133:
						{return new Symbol(TokenConstants.OBJECTID, new StringSymbol(yytext(), yytext().length(), 0));}
					case -130:
						break;
					case 134:
						{return new Symbol(TokenConstants.TYPEID, new StringSymbol(yytext(), yytext().length(), 0));}
					case -131:
						break;
					case 135:
						{return new Symbol(TokenConstants.OBJECTID, new StringSymbol(yytext(), yytext().length(), 0));}
					case -132:
						break;
					case 136:
						{return new Symbol(TokenConstants.TYPEID, new StringSymbol(yytext(), yytext().length(), 0));}
					case -133:
						break;
					case 137:
						{return new Symbol(TokenConstants.OBJECTID, new StringSymbol(yytext(), yytext().length(), 0));}
					case -134:
						break;
					case 138:
						{return new Symbol(TokenConstants.TYPEID, new StringSymbol(yytext(), yytext().length(), 0));}
					case -135:
						break;
					case 139:
						{return new Symbol(TokenConstants.OBJECTID, new StringSymbol(yytext(), yytext().length(), 0));}
					case -136:
						break;
					case 140:
						{return new Symbol(TokenConstants.TYPEID, new StringSymbol(yytext(), yytext().length(), 0));}
					case -137:
						break;
					case 141:
						{return new Symbol(TokenConstants.OBJECTID, new StringSymbol(yytext(), yytext().length(), 0));}
					case -138:
						break;
					case 142:
						{return new Symbol(TokenConstants.TYPEID, new StringSymbol(yytext(), yytext().length(), 0));}
					case -139:
						break;
					case 143:
						{return new Symbol(TokenConstants.OBJECTID, new StringSymbol(yytext(), yytext().length(), 0));}
					case -140:
						break;
					case 144:
						{return new Symbol(TokenConstants.TYPEID, new StringSymbol(yytext(), yytext().length(), 0));}
					case -141:
						break;
					case 145:
						{return new Symbol(TokenConstants.OBJECTID, new StringSymbol(yytext(), yytext().length(), 0));}
					case -142:
						break;
					case 146:
						{return new Symbol(TokenConstants.TYPEID, new StringSymbol(yytext(), yytext().length(), 0));}
					case -143:
						break;
					case 147:
						{return new Symbol(TokenConstants.OBJECTID, new StringSymbol(yytext(), yytext().length(), 0));}
					case -144:
						break;
					case 148:
						{return new Symbol(TokenConstants.TYPEID, new StringSymbol(yytext(), yytext().length(), 0));}
					case -145:
						break;
					case 149:
						{return new Symbol(TokenConstants.OBJECTID, new StringSymbol(yytext(), yytext().length(), 0));}
					case -146:
						break;
					case 150:
						{return new Symbol(TokenConstants.TYPEID, new StringSymbol(yytext(), yytext().length(), 0));}
					case -147:
						break;
					case 151:
						{return new Symbol(TokenConstants.OBJECTID, new StringSymbol(yytext(), yytext().length(), 0));}
					case -148:
						break;
					case 152:
						{return new Symbol(TokenConstants.TYPEID, new StringSymbol(yytext(), yytext().length(), 0));}
					case -149:
						break;
					case 153:
						{return new Symbol(TokenConstants.OBJECTID, new StringSymbol(yytext(), yytext().length(), 0));}
					case -150:
						break;
					case 154:
						{return new Symbol(TokenConstants.TYPEID, new StringSymbol(yytext(), yytext().length(), 0));}
					case -151:
						break;
					case 155:
						{return new Symbol(TokenConstants.OBJECTID, new StringSymbol(yytext(), yytext().length(), 0));}
					case -152:
						break;
					case 156:
						{return new Symbol(TokenConstants.TYPEID, new StringSymbol(yytext(), yytext().length(), 0));}
					case -153:
						break;
					case 157:
						{return new Symbol(TokenConstants.OBJECTID, new StringSymbol(yytext(), yytext().length(), 0));}
					case -154:
						break;
					case 158:
						{return new Symbol(TokenConstants.TYPEID, new StringSymbol(yytext(), yytext().length(), 0));}
					case -155:
						break;
					case 159:
						{return new Symbol(TokenConstants.OBJECTID, new StringSymbol(yytext(), yytext().length(), 0));}
					case -156:
						break;
					case 160:
						{return new Symbol(TokenConstants.OBJECTID, new StringSymbol(yytext(), yytext().length(), 0));}
					case -157:
						break;
					case 161:
						{return new Symbol(TokenConstants.OBJECTID, new StringSymbol(yytext(), yytext().length(), 0));}
					case -158:
						break;
					case 162:
						{return new Symbol(TokenConstants.OBJECTID, new StringSymbol(yytext(), yytext().length(), 0));}
					case -159:
						break;
					case 163:
						{return new Symbol(TokenConstants.TYPEID, new StringSymbol(yytext(), yytext().length(), 0));}
					case -160:
						break;
					case 164:
						{return new Symbol(TokenConstants.OBJECTID, new StringSymbol(yytext(), yytext().length(), 0));}
					case -161:
						break;
					case 165:
						{return new Symbol(TokenConstants.TYPEID, new StringSymbol(yytext(), yytext().length(), 0));}
					case -162:
						break;
					case 166:
						{return new Symbol(TokenConstants.OBJECTID, new StringSymbol(yytext(), yytext().length(), 0));}
					case -163:
						break;
					case 167:
						{return new Symbol(TokenConstants.TYPEID, new StringSymbol(yytext(), yytext().length(), 0));}
					case -164:
						break;
					case 168:
						{return new Symbol(TokenConstants.OBJECTID, new StringSymbol(yytext(), yytext().length(), 0));}
					case -165:
						break;
					case 169:
						{return new Symbol(TokenConstants.TYPEID, new StringSymbol(yytext(), yytext().length(), 0));}
					case -166:
						break;
					case 170:
						{return new Symbol(TokenConstants.OBJECTID, new StringSymbol(yytext(), yytext().length(), 0));}
					case -167:
						break;
					case 171:
						{return new Symbol(TokenConstants.TYPEID, new StringSymbol(yytext(), yytext().length(), 0));}
					case -168:
						break;
					case 172:
						{return new Symbol(TokenConstants.OBJECTID, new StringSymbol(yytext(), yytext().length(), 0));}
					case -169:
						break;
					case 173:
						{return new Symbol(TokenConstants.TYPEID, new StringSymbol(yytext(), yytext().length(), 0));}
					case -170:
						break;
					default:
						yy_error(YY_E_INTERNAL,false);
					case -1:
					}
					yy_initial = true;
					yy_state = yy_state_dtrans[yy_lexical_state];
					yy_next_state = YY_NO_STATE;
					yy_last_accept_state = YY_NO_STATE;
					yy_mark_start();
					yy_this_accept = yy_acpt[yy_state];
					if (YY_NOT_ACCEPT != yy_this_accept) {
						yy_last_accept_state = yy_state;
						yy_mark_end();
					}
				}
			}
		}
	}
}
