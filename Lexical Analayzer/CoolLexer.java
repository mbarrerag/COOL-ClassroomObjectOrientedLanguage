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
    private int parent_len = 0;
    int get_paren_len(){
      return parent_len;
    }
    void reset_parent_len (){
      parent_len = 0;
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
    // empty for now
	}

	private boolean yy_eof_done = false;
	private final int LINE_COMMENT = 2;
	private final int BLOCK_COMMENT = 1;
	private final int YYINITIAL = 0;
	private final int STRING_MODE = 3;
	private final int yy_state_dtrans[] = {
		0,
		55,
		70,
		95
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
		/* 65 */ YY_NO_ANCHOR,
		/* 66 */ YY_NO_ANCHOR,
		/* 67 */ YY_NO_ANCHOR,
		/* 68 */ YY_NO_ANCHOR,
		/* 69 */ YY_NO_ANCHOR,
		/* 70 */ YY_NOT_ACCEPT,
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
		/* 87 */ YY_NO_ANCHOR,
		/* 88 */ YY_NO_ANCHOR,
		/* 89 */ YY_NO_ANCHOR,
		/* 90 */ YY_NO_ANCHOR,
		/* 91 */ YY_NO_ANCHOR,
		/* 92 */ YY_NO_ANCHOR,
		/* 93 */ YY_NO_ANCHOR,
		/* 94 */ YY_NO_ANCHOR,
		/* 95 */ YY_NOT_ACCEPT,
		/* 96 */ YY_NO_ANCHOR,
		/* 97 */ YY_NO_ANCHOR,
		/* 98 */ YY_NO_ANCHOR,
		/* 99 */ YY_NO_ANCHOR,
		/* 100 */ YY_NO_ANCHOR,
		/* 101 */ YY_NOT_ACCEPT,
		/* 102 */ YY_NO_ANCHOR,
		/* 103 */ YY_NO_ANCHOR,
		/* 104 */ YY_NOT_ACCEPT,
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
		/* 173 */ YY_NO_ANCHOR,
		/* 174 */ YY_NO_ANCHOR,
		/* 175 */ YY_NO_ANCHOR,
		/* 176 */ YY_NO_ANCHOR,
		/* 177 */ YY_NO_ANCHOR,
		/* 178 */ YY_NO_ANCHOR,
		/* 179 */ YY_NO_ANCHOR,
		/* 180 */ YY_NO_ANCHOR,
		/* 181 */ YY_NO_ANCHOR,
		/* 182 */ YY_NO_ANCHOR,
		/* 183 */ YY_NO_ANCHOR
	};
	private int yy_cmap[] = unpackFromString(1,130,
"2,5:7,17,16,4,7,18,3,5:18,15,5,1,5:5,8,10,9,57,61,11,59,58,38:10,63,62,14,1" +
"2,13,5,64,39,40,41,42,43,25,40,44,45,40:2,46,40,47,48,49,40,50,51,30,52,53," +
"54,40:3,5,6,5:2,55,5,20,56,19,33,22,24,56,28,26,56:2,23,56,27,32,34,56,29,2" +
"1,36,37,31,35,56:3,66,5,65,60,5,0:2")[0];

	private int yy_rmap[] = unpackFromString(1,184,
"0,1:3,2,1,3,4,5,1,6,7,8,9,10,11,12,13,14,15,1:16,16:2,17,16:8,18,16:7,19,1," +
"20,1:2,21,1,21:2,1,22,1:4,23,24,25,18:2,26,18:8,16,18:5,27,28,29,1,30,31,32" +
",33,1,34,35,22,36,37,30,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54," +
"55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79," +
"80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,102,103" +
",104,105,106,16,18,107,108,109,110,111,112,113,114")[0];

	private int yy_nxt[][] = unpackFromString(115,67,
"1,2,3,4,5,3:2,6,7,8,9,10,11,3,12,13,14,15,16,17,174:2,176,133,71,18,96,135," +
"174:2,72,174,102,174,178,180,182,174,19,175:2,177,175,179,175,97,134,136,10" +
"3,181,175:4,183,3,174,20,21,22,23,24,25,26,27,28,29,-1:70,4,-1:70,6,-1:68,3" +
"0,-1:67,31,-1:67,32,-1:68,33,-1:64,34,35,-1:69,13,-1:67,14,-1:67,15,-1:67,1" +
"6,-1:67,174,137,174:2,139,174:15,137,174:6,139,174:10,-1:29,175:7,73,175:18" +
",73,175:11,-1:48,19,-1:47,174:38,-1:29,174:9,165,174:15,165,174:12,-1:29,17" +
"5:38,-1:10,1,90:3,56,90:3,57,91,98,90:56,-1:9,58,-1:58,60:3,-1,60:62,-1,67," +
"101:65,1,60:3,61,60:3,92,99,60:57,-1:19,174,147,174:5,36,174:12,147,174:5,3" +
"6,174:11,-1:29,175:9,138,175:15,138,175:12,-1:29,175:9,160,175:15,160,175:1" +
"2,-1:11,90:3,-1,90:3,-1:3,90:56,-1:10,59,-1:57,60:3,-1,60:4,62,60:57,-1:3,1" +
"04,69,-1:2,104,-1:7,104:4,-1:48,1,64,65,-1,66,93,100,93:60,-1:19,174:2,149," +
"174:2,37:2,174,38,174:19,38,174:3,149,174:5,-1:29,175:2,148,175:2,74:2,175," +
"75,175:19,75,175:3,148,175:5,-1:11,60:3,-1,60:5,63,60:56,-1,68:2,104,69,68:" +
"2,94,68:7,94:4,68:48,-1:19,174:5,39:2,174:31,-1:29,175:5,76:2,175:31,-1:29," +
"174:11,40,174:5,40,174:20,-1:29,175:11,77,175:5,77,175:20,-1:29,174:16,41,1" +
"74:18,41,174:2,-1:29,175:16,78,175:18,78,175:2,-1:29,174:11,42,174:5,42,174" +
":20,-1:29,175:11,79,175:5,79,175:20,-1:29,174:3,43,174:20,43,174:13,-1:29,1" +
"75:8,47,175:19,47,175:9,-1:29,44,174:21,44,174:15,-1:29,175:3,80,175:20,80," +
"175:13,-1:29,174:3,45,174:20,45,174:13,-1:29,81,175:21,81,175:15,-1:29,174:" +
"15,46,174:14,46,174:7,-1:29,175:3,82,175:20,82,175:13,-1:29,174:4,48,174:22" +
",48,174:10,-1:29,175:15,83,175:14,83,175:7,-1:29,174:8,84,174:19,84,174:9,-" +
"1:29,175:4,85,175:22,85,175:10,-1:29,174:3,49,174:20,49,174:13,-1:29,175:2," +
"86,175:29,86,175:5,-1:29,174:2,50,174:29,50,174:5,-1:29,175:3,87,175:20,87," +
"175:13,-1:29,174:3,51,174:20,51,174:13,-1:29,175:14,88,175:8,88,175:14,-1:2" +
"9,174:3,52,174:20,52,174:13,-1:29,175:2,89,175:29,89,175:5,-1:29,174:14,53," +
"174:8,53,174:14,-1:29,174:2,54,174:29,54,174:5,-1:29,174:3,105,174:9,145,17" +
"4:10,105,174:4,145,174:8,-1:29,175:3,106,175:9,150,175:10,106,175:4,150,175" +
":8,-1:29,174:3,107,174:9,109,174:10,107,174:4,109,174:8,-1:29,175:3,108,175" +
":9,110,175:10,108,175:4,110,175:8,-1:29,174:2,111,174:29,111,174:5,-1:29,17" +
"5:3,112,175:20,112,175:13,-1:29,174,159,174:18,159,174:17,-1:29,175:2,114,1" +
"75:29,114,175:5,-1:29,174,113,174:18,113,174:17,-1:29,175,156,175:18,156,17" +
"5:17,-1:29,174:2,115,174:29,115,174:5,-1:29,175,116,175:18,116,175:17,-1:29" +
",174:13,117,174:15,117,174:8,-1:29,175:2,118,175:29,118,175:5,-1:29,174:4,1" +
"61,174:22,161,174:10,-1:29,175:12,158,175:21,158,175:3,-1:29,174:12,163,174" +
":21,163,174:3,-1:29,175:13,120,175:15,120,175:8,-1:29,174:13,119,174:15,119" +
",174:8,-1:29,175:13,122,175:15,122,175:8,-1:29,174:7,167,174:18,167,174:11," +
"-1:29,175:7,162,175:18,162,175:11,-1:29,174:3,121,174:20,121,174:13,-1:29,1" +
"75:2,124,175:29,124,175:5,-1:29,174:18,123,174:14,123,174:4,-1:29,175:13,16" +
"4,175:15,164,175:8,-1:29,174:2,125,174:29,125,174:5,-1:29,175:3,166,175:20," +
"166,175:13,-1:29,174:2,127,174:29,127,174:5,-1:29,175:4,126,175:22,126,175:" +
"10,-1:29,174:13,169,174:15,169,174:8,-1:29,175:7,128,175:18,128,175:11,-1:2" +
"9,174:3,171,174:20,171,174:13,-1:29,175:10,168,175:20,168,175:6,-1:29,174:4" +
",129,174:22,129,174:10,-1:29,175:7,170,175:18,170,175:11,-1:29,174:7,131,17" +
"4:18,131,174:11,-1:29,175:11,130,175:5,130,175:20,-1:29,174:10,172,174:20,1" +
"72,174:6,-1:29,174:7,173,174:18,173,174:11,-1:29,174:11,132,174:5,132,174:2" +
"0,-1:29,174:2,141,174,143,174:22,143,174:4,141,174:5,-1:29,175,140,175:2,14" +
"2,175:15,140,175:6,142,175:10,-1:29,174:13,151,174:15,151,174:8,-1:29,175:2" +
",144,175,146,175:22,146,175:4,144,175:5,-1:29,174:9,153,174:15,153,174:12,-" +
"1:29,175:13,152,175:15,152,175:8,-1:29,174:9,155,157,174:14,155,174:5,157,1" +
"74:6,-1:29,175:9,154,175:15,154,175:12,-1:10");

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
      return new Symbol(TokenConstants.EOF);
    case LINE_COMMENT:
      yybegin(YYINITIAL); 
	break;
    case BLOCK_COMMENT:
      yybegin(YYINITIAL);
      return new Symbol(TokenConstants.ERROR, "eof is comment" );
    case STRING_MODE:
       yybegin(YYINITIAL);
      string_buf = new StringBuffer();
      return new Symbol(TokenConstants.ERROR, "eof is string comment"); 
    }   
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
						{ yybegin(STRING_MODE); }
					case -3:
						break;
					case 3:
						{return new Symbol(TokenConstants.ERROR, yytext()); }
					case -4:
						break;
					case 4:
						{ /* do nothing just eat it up */ }
					case -5:
						break;
					case 5:
						{ curr_lineno++; }
					case -6:
						break;
					case 6:
						{ /* do nothing just eat it up */ }
					case -7:
						break;
					case 7:
						{ return new Symbol(TokenConstants.LPAREN); }
					case -8:
						break;
					case 8:
						{ return new Symbol(TokenConstants.MULT); }
					case -9:
						break;
					case 9:
						{ return new Symbol(TokenConstants.RPAREN); }
					case -10:
						break;
					case 10:
						{ return new Symbol(TokenConstants.MINUS); }
					case -11:
						break;
					case 11:
						{ return new Symbol(TokenConstants.EQ); }
					case -12:
						break;
					case 12:
						{ return new Symbol(TokenConstants.LT); }
					case -13:
						break;
					case 13:
						{ /* do nothing just eat it up */ }
					case -14:
						break;
					case 14:
						{ /* do nothing just eat it up */ }
					case -15:
						break;
					case 15:
						{ /* do nothing just eat it up */ }
					case -16:
						break;
					case 16:
						{ /* do nothing just eat it up */ }
					case -17:
						break;
					case 17:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.stringtable.addString(yytext())); }
					case -18:
						break;
					case 18:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.stringtable.addString(yytext())); }
					case -19:
						break;
					case 19:
						{ return new Symbol(TokenConstants.INT_CONST, AbstractTable.inttable.addString(yytext()));      }
					case -20:
						break;
					case 20:
						{ return new Symbol(TokenConstants.PLUS); }
					case -21:
						break;
					case 21:
						{ return new Symbol(TokenConstants.DIV); }
					case -22:
						break;
					case 22:
						{ return new Symbol(TokenConstants.DOT); }
					case -23:
						break;
					case 23:
						{ return new Symbol(TokenConstants.NEG); }
					case -24:
						break;
					case 24:
						{ return new Symbol(TokenConstants.COMMA); }
					case -25:
						break;
					case 25:
						{ return new Symbol(TokenConstants.SEMI); }
					case -26:
						break;
					case 26:
						{ return new Symbol(TokenConstants.COLON); }
					case -27:
						break;
					case 27:
						{ return new Symbol(TokenConstants.AT); }
					case -28:
						break;
					case 28:
						{ return new Symbol(TokenConstants.RBRACE); }
					case -29:
						break;
					case 29:
						{ return new Symbol(TokenConstants.LBRACE); }
					case -30:
						break;
					case 30:
						{ parent_len++; yybegin(BLOCK_COMMENT);}
					case -31:
						break;
					case 31:
						{ return new Symbol(TokenConstants.ERROR, "Unmatched *)");}
					case -32:
						break;
					case 32:
						{yybegin(LINE_COMMENT); }
					case -33:
						break;
					case 33:
						{ return new Symbol(TokenConstants.DARROW); }
					case -34:
						break;
					case 34:
						{ return new Symbol(TokenConstants.ASSIGN); }
					case -35:
						break;
					case 35:
						{ return new Symbol(TokenConstants.LE); }
					case -36:
						break;
					case 36:
						{ return new Symbol(TokenConstants.FI); }
					case -37:
						break;
					case 37:
						{ return new Symbol(TokenConstants.IF); }
					case -38:
						break;
					case 38:
						{ return new Symbol(TokenConstants.IN); }
					case -39:
						break;
					case 39:
						{ return new Symbol(TokenConstants.OF); }
					case -40:
						break;
					case 40:
						{ return new Symbol(TokenConstants.LET); }
					case -41:
						break;
					case 41:
						{ return new Symbol(TokenConstants.NEW); }
					case -42:
						break;
					case 42:
						{ return new Symbol(TokenConstants.NOT); }
					case -43:
						break;
					case 43:
						{ return new Symbol(TokenConstants.CASE); }
					case -44:
						break;
					case 44:
						{ return new Symbol(TokenConstants.ESAC); }
					case -45:
						break;
					case 45:
						{ return new Symbol(TokenConstants.ELSE); }
					case -46:
						break;
					case 46:
						{ return new Symbol(TokenConstants.LOOP); }
					case -47:
						break;
					case 47:
						{ return new Symbol(TokenConstants.THEN); }
					case -48:
						break;
					case 48:
						{ return new Symbol(TokenConstants.POOL); }
					case -49:
						break;
					case 49:
						{ return new Symbol(TokenConstants.BOOL_CONST, Boolean.TRUE); }
					case -50:
						break;
					case 50:
						{ return new Symbol(TokenConstants.CLASS); }
					case -51:
						break;
					case 51:
						{ return new Symbol(TokenConstants.BOOL_CONST, Boolean.FALSE);}
					case -52:
						break;
					case 52:
						{ return new Symbol(TokenConstants.WHILE); }
					case -53:
						break;
					case 53:
						{ return new Symbol(TokenConstants.ISVOID); }
					case -54:
						break;
					case 54:
						{ return new Symbol(TokenConstants.INHERITS); }
					case -55:
						break;
					case 55:
						{/* It skips the mayority of the characters less *characters (),  \n *  */}
					case -56:
						break;
					case 56:
						{ curr_lineno++; }
					case -57:
						break;
					case 57:
						{ }
					case -58:
						break;
					case 58:
						{ parent_len++; }
					case -59:
						break;
					case 59:
						{ 
                    parent_len--;  
		    if(parent_len < 0){
                         return new Symbol(TokenConstants.ERROR, "Error:)");
                   } else if(parent_len == 0) {
                       yybegin(YYINITIAL);  
                   } else {
                  }
 }
					case -60:
						break;
					case 60:
						{/* Do nothing */}
					case -61:
						break;
					case 61:
						{curr_lineno++; yybegin(YYINITIAL);}
					case -62:
						break;
					case 62:
						{   }
					case -63:
						break;
					case 63:
						{   }
					case -64:
						break;
					case 64:
						{ /* Finds the begging of the string*/
  yybegin(YYINITIAL);
  if(string_buf.length() > MAX_STR_CONST){
    return new Symbol(TokenConstants.ERROR, "String too long");
      } else {
        String s = string_buf.toString();
	string_buf = new StringBuffer();
	return new Symbol(TokenConstants.STR_CONST, AbstractTable.stringtable.addString(s));
  } 
 }
					case -65:
						break;
					case 65:
						{ 
  string_buf = string_buf.append(yytext().charAt(0));
}
					case -66:
						break;
					case 66:
						{ 
  yybegin(YYINITIAL);
  string_buf = new StringBuffer();
  curr_lineno++;
  return new Symbol(TokenConstants.ERROR, "String contains null character");
}
					case -67:
						break;
					case 67:
						{
/* Finds a empty string with more than one string*/
  yybegin(YYINITIAL);
  string_buf = new StringBuffer();
  return new Symbol(TokenConstants.ERROR, "String contains escaped null character"); 
}
					case -68:
						break;
					case 68:
						{ 
                    if (yytext().equals("\\n")) {
                        string_buf = string_buf.append('\n'); 
                    } else if (yytext().equals("\\b")){
                        string_buf = string_buf.append('\b'); 
                    } else if (yytext().equals("\\t")){
                        string_buf = string_buf.append('\t'); 
                    } else if (yytext().equals("\\f")){
                        string_buf = string_buf.append('\f'); 
                    } else if (yytext().equals("\\\000")){
                        yybegin(YYINITIAL);
                        string_buf = new StringBuffer();
                        return new Symbol(TokenConstants.ERROR, "String contains null character");
                    } else {
                        string_buf = string_buf.append(yytext().charAt(1));
                    }
                }
					case -69:
						break;
					case 69:
						{ 
  string_buf = string_buf.append("\n");
  curr_lineno++;
}
					case -70:
						break;
					case 71:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.stringtable.addString(yytext())); }
					case -71:
						break;
					case 72:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.stringtable.addString(yytext())); }
					case -72:
						break;
					case 73:
						{ return new Symbol(TokenConstants.FI); }
					case -73:
						break;
					case 74:
						{ return new Symbol(TokenConstants.IF); }
					case -74:
						break;
					case 75:
						{ return new Symbol(TokenConstants.IN); }
					case -75:
						break;
					case 76:
						{ return new Symbol(TokenConstants.OF); }
					case -76:
						break;
					case 77:
						{ return new Symbol(TokenConstants.LET); }
					case -77:
						break;
					case 78:
						{ return new Symbol(TokenConstants.NEW); }
					case -78:
						break;
					case 79:
						{ return new Symbol(TokenConstants.NOT); }
					case -79:
						break;
					case 80:
						{ return new Symbol(TokenConstants.CASE); }
					case -80:
						break;
					case 81:
						{ return new Symbol(TokenConstants.ESAC); }
					case -81:
						break;
					case 82:
						{ return new Symbol(TokenConstants.ELSE); }
					case -82:
						break;
					case 83:
						{ return new Symbol(TokenConstants.LOOP); }
					case -83:
						break;
					case 84:
						{ return new Symbol(TokenConstants.THEN); }
					case -84:
						break;
					case 85:
						{ return new Symbol(TokenConstants.POOL); }
					case -85:
						break;
					case 86:
						{ return new Symbol(TokenConstants.CLASS); }
					case -86:
						break;
					case 87:
						{ return new Symbol(TokenConstants.WHILE); }
					case -87:
						break;
					case 88:
						{ return new Symbol(TokenConstants.ISVOID); }
					case -88:
						break;
					case 89:
						{ return new Symbol(TokenConstants.INHERITS); }
					case -89:
						break;
					case 90:
						{/* It skips the mayority of the characters less *characters (),  \n *  */}
					case -90:
						break;
					case 91:
						{ }
					case -91:
						break;
					case 92:
						{/* Do nothing */}
					case -92:
						break;
					case 93:
						{ 
  string_buf = string_buf.append(yytext().charAt(0));
}
					case -93:
						break;
					case 94:
						{ 
                    if (yytext().equals("\\n")) {
                        string_buf = string_buf.append('\n'); 
                    } else if (yytext().equals("\\b")){
                        string_buf = string_buf.append('\b'); 
                    } else if (yytext().equals("\\t")){
                        string_buf = string_buf.append('\t'); 
                    } else if (yytext().equals("\\f")){
                        string_buf = string_buf.append('\f'); 
                    } else if (yytext().equals("\\\000")){
                        yybegin(YYINITIAL);
                        string_buf = new StringBuffer();
                        return new Symbol(TokenConstants.ERROR, "String contains null character");
                    } else {
                        string_buf = string_buf.append(yytext().charAt(1));
                    }
                }
					case -94:
						break;
					case 96:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.stringtable.addString(yytext())); }
					case -95:
						break;
					case 97:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.stringtable.addString(yytext())); }
					case -96:
						break;
					case 98:
						{ }
					case -97:
						break;
					case 99:
						{/* Do nothing */}
					case -98:
						break;
					case 100:
						{ 
  string_buf = string_buf.append(yytext().charAt(0));
}
					case -99:
						break;
					case 102:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.stringtable.addString(yytext())); }
					case -100:
						break;
					case 103:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.stringtable.addString(yytext())); }
					case -101:
						break;
					case 105:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.stringtable.addString(yytext())); }
					case -102:
						break;
					case 106:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.stringtable.addString(yytext())); }
					case -103:
						break;
					case 107:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.stringtable.addString(yytext())); }
					case -104:
						break;
					case 108:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.stringtable.addString(yytext())); }
					case -105:
						break;
					case 109:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.stringtable.addString(yytext())); }
					case -106:
						break;
					case 110:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.stringtable.addString(yytext())); }
					case -107:
						break;
					case 111:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.stringtable.addString(yytext())); }
					case -108:
						break;
					case 112:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.stringtable.addString(yytext())); }
					case -109:
						break;
					case 113:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.stringtable.addString(yytext())); }
					case -110:
						break;
					case 114:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.stringtable.addString(yytext())); }
					case -111:
						break;
					case 115:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.stringtable.addString(yytext())); }
					case -112:
						break;
					case 116:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.stringtable.addString(yytext())); }
					case -113:
						break;
					case 117:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.stringtable.addString(yytext())); }
					case -114:
						break;
					case 118:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.stringtable.addString(yytext())); }
					case -115:
						break;
					case 119:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.stringtable.addString(yytext())); }
					case -116:
						break;
					case 120:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.stringtable.addString(yytext())); }
					case -117:
						break;
					case 121:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.stringtable.addString(yytext())); }
					case -118:
						break;
					case 122:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.stringtable.addString(yytext())); }
					case -119:
						break;
					case 123:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.stringtable.addString(yytext())); }
					case -120:
						break;
					case 124:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.stringtable.addString(yytext())); }
					case -121:
						break;
					case 125:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.stringtable.addString(yytext())); }
					case -122:
						break;
					case 126:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.stringtable.addString(yytext())); }
					case -123:
						break;
					case 127:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.stringtable.addString(yytext())); }
					case -124:
						break;
					case 128:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.stringtable.addString(yytext())); }
					case -125:
						break;
					case 129:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.stringtable.addString(yytext())); }
					case -126:
						break;
					case 130:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.stringtable.addString(yytext())); }
					case -127:
						break;
					case 131:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.stringtable.addString(yytext())); }
					case -128:
						break;
					case 132:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.stringtable.addString(yytext())); }
					case -129:
						break;
					case 133:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.stringtable.addString(yytext())); }
					case -130:
						break;
					case 134:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.stringtable.addString(yytext())); }
					case -131:
						break;
					case 135:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.stringtable.addString(yytext())); }
					case -132:
						break;
					case 136:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.stringtable.addString(yytext())); }
					case -133:
						break;
					case 137:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.stringtable.addString(yytext())); }
					case -134:
						break;
					case 138:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.stringtable.addString(yytext())); }
					case -135:
						break;
					case 139:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.stringtable.addString(yytext())); }
					case -136:
						break;
					case 140:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.stringtable.addString(yytext())); }
					case -137:
						break;
					case 141:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.stringtable.addString(yytext())); }
					case -138:
						break;
					case 142:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.stringtable.addString(yytext())); }
					case -139:
						break;
					case 143:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.stringtable.addString(yytext())); }
					case -140:
						break;
					case 144:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.stringtable.addString(yytext())); }
					case -141:
						break;
					case 145:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.stringtable.addString(yytext())); }
					case -142:
						break;
					case 146:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.stringtable.addString(yytext())); }
					case -143:
						break;
					case 147:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.stringtable.addString(yytext())); }
					case -144:
						break;
					case 148:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.stringtable.addString(yytext())); }
					case -145:
						break;
					case 149:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.stringtable.addString(yytext())); }
					case -146:
						break;
					case 150:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.stringtable.addString(yytext())); }
					case -147:
						break;
					case 151:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.stringtable.addString(yytext())); }
					case -148:
						break;
					case 152:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.stringtable.addString(yytext())); }
					case -149:
						break;
					case 153:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.stringtable.addString(yytext())); }
					case -150:
						break;
					case 154:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.stringtable.addString(yytext())); }
					case -151:
						break;
					case 155:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.stringtable.addString(yytext())); }
					case -152:
						break;
					case 156:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.stringtable.addString(yytext())); }
					case -153:
						break;
					case 157:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.stringtable.addString(yytext())); }
					case -154:
						break;
					case 158:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.stringtable.addString(yytext())); }
					case -155:
						break;
					case 159:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.stringtable.addString(yytext())); }
					case -156:
						break;
					case 160:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.stringtable.addString(yytext())); }
					case -157:
						break;
					case 161:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.stringtable.addString(yytext())); }
					case -158:
						break;
					case 162:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.stringtable.addString(yytext())); }
					case -159:
						break;
					case 163:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.stringtable.addString(yytext())); }
					case -160:
						break;
					case 164:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.stringtable.addString(yytext())); }
					case -161:
						break;
					case 165:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.stringtable.addString(yytext())); }
					case -162:
						break;
					case 166:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.stringtable.addString(yytext())); }
					case -163:
						break;
					case 167:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.stringtable.addString(yytext())); }
					case -164:
						break;
					case 168:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.stringtable.addString(yytext())); }
					case -165:
						break;
					case 169:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.stringtable.addString(yytext())); }
					case -166:
						break;
					case 170:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.stringtable.addString(yytext())); }
					case -167:
						break;
					case 171:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.stringtable.addString(yytext())); }
					case -168:
						break;
					case 172:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.stringtable.addString(yytext())); }
					case -169:
						break;
					case 173:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.stringtable.addString(yytext())); }
					case -170:
						break;
					case 174:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.stringtable.addString(yytext())); }
					case -171:
						break;
					case 175:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.stringtable.addString(yytext())); }
					case -172:
						break;
					case 176:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.stringtable.addString(yytext())); }
					case -173:
						break;
					case 177:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.stringtable.addString(yytext())); }
					case -174:
						break;
					case 178:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.stringtable.addString(yytext())); }
					case -175:
						break;
					case 179:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.stringtable.addString(yytext())); }
					case -176:
						break;
					case 180:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.stringtable.addString(yytext())); }
					case -177:
						break;
					case 181:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.stringtable.addString(yytext())); }
					case -178:
						break;
					case 182:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.stringtable.addString(yytext())); }
					case -179:
						break;
					case 183:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.stringtable.addString(yytext())); }
					case -180:
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
