package sgf;

import logic.board.Board;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static util.Coords.getCoords;
import static util.Move.play;
import static util.StoneColour.BLACK;

public class SGFReaderTest {

	private static String simpleSGF = "(;FF[4]GM[1]SZ[19];B[aa])";

	@Test
	public void readSGF() {
		SGFReader reader = new SGFReader(simpleSGF);
		Board board = new Board();
		board.playStone(play(getCoords("A1"), BLACK));

		assertThat(reader.getGameTree().getPosition(), is(board));
	}

	private String loadFile(String filepath) {
		InputStreamReader is = null;
		BufferedReader reader = null;
		StringBuilder builder = new StringBuilder();

		try {
			is = new FileReader(filepath);
			reader = new BufferedReader(is);

			String currentLine = null;
			while ((currentLine = reader.readLine()) != null) {
				builder.append(currentLine);
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
				if ( is != null )
					is.close();

				if ( reader != null )
					reader.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}

		return builder.toString();
	}
}