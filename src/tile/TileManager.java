package tile;

import java.io.*;
import java.util.*;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import rngGAME.SpielPanel;

public class TileManager extends Pane {

	SpielPanel gp;
	List<Tile> tile;
	int mapTileNum[][];
	Group group;


	public TileManager(SpielPanel gp) {
		this.gp = gp;

		tile = new ArrayList<>();
		mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];

		getTileImage();
		loadMap("/maps/lavaMap.txt");
	}

	public void getTileImage() {

		try {
			//
			//			Arrays.asList(new File(getClass().getResource("/grastiles").toURI()).listFiles()).forEach(f -> {
			//				try {
			//					System.out.println(f);
			//					tile.add(new Tile(new Image(new FileInputStream(f))));
			//				} catch (FileNotFoundException e) {
			//					e.printStackTrace();
			//				}
			//			});

			tile.add(new Tile(getClass().getResourceAsStream("/tiles/Stein.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/tiles/LavaS3.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/grastiles/SandOG.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/grastiles/SGELO.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/grastiles/SGGSU.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/grastiles/SGMELO.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/grastiles/Sand.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/grastiles/GrasBoden.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/grastiles/SGEMRU.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/grastiles/Pflanze.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/grastiles/SandOGL.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/grastiles/SGGSR.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/grastiles/SGGMMERU.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/grastiles/SGGSL.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/grastiles/SGGSU.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/grastiles/SGELU.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/grastiles/SGGSOW.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/grastiles/SGGMMERO.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/grastiles/SandOGU.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/grastiles/Wasser.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/grastiles/SteinWandWasser.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/grastiles/SELU.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/grastiles/SteinWandWasserGerade.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/grastiles/SteinWandWasserEcke.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/grastiles/SteinWandWasserEckeC.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/grastiles/SteinWandWasserEckeR.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/grastiles/SteinWandWasserEckeF.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/grastiles/SteinWandWasserEckeRR.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/grastiles/GrasEcke.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/grastiles/SGGSO.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/grastiles/SteinWandWasserEckeRG.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/grastiles/SGERU.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/grastiles/GrasEckeLO.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/grastiles/GrasSas.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/grastiles/SteinWandWasserEckeRU.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/grastiles/SteinWandWasserEckeRRU.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/grastiles/SERUU.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/grastiles/SGERO.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/grastiles/GrasEckeLU.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/grastiles/SteinWandWasserEckeLU.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/grastiles/SteinWandWasserEckeLLU.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/grastiles/GrasEckeFF.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/grastiles/Hoffnung.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/grastiles/Hope2.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/grastiles/SteinWandWasserEckeLG.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/grastiles/Hope3.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/grastiles/Hope4.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/grastiles/Hope5.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/grastiles/Gras.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/grastiles/Hope6.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/grastiles/DGERU.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/grastiles/AWDAD.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/grastiles/GEGEGE.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/grastiles/DGERU2.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/grastiles/AWDAD2.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/grastiles/GEGEGE2.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/grastiles/DGERU3.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/grastiles/AWDAD3.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/grastiles/GEGEGE3.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/grastiles/DGERU4.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/grastiles/AWDAD4.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/grastiles/GEGEGE4.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/grastiles/Nanana.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/grastiles/Puff.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/grastiles/FFFF.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/grastiles/Zaun.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/grastiles/Zaun2.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/grastiles/ZaunS.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/grastiles/ZaunF.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/grastiles/ZaunFF.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/grastiles/ZaunSS.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/grastiles/DM.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/grastiles/DL.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/grastiles/Eingang.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/grastiles/HL.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/grastiles/DR.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/grastiles/HR.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/grastiles/HSL.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/grastiles/HSRU.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/grastiles/HSLU.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/grastiles/HSRO.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/grastiles/HSLO.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/grastiles/HSR.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/grastiles/Baum.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/grastiles/ALMU.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/grastiles/ALEU.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/grastiles/ALSML.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/grastiles/ALEO.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/grastiles/AMF.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/grastiles/ALSMO.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/grastiles/AREO.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/grastiles/ALSMR.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/grastiles/AREU.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/grastiles/SandOGR.png"), gp));

		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	public void loadMap(String filePath) {

		int col = 0;
		int row = 0;

		try {

			InputStream is = getClass().getResourceAsStream(filePath);			//Importiert Die Textdatei
			BufferedReader br = new BufferedReader(new InputStreamReader(is));

			while (col < gp.maxWorldCol && row < gp.maxWorldRow) {

				String line = br.readLine();

				while (col < gp.maxWorldCol) {

					String numbers[] = line.split(" ");

					int num = Integer.parseInt(numbers[col]);

					mapTileNum[col][row] = num;
					col++;

				}
				if (col == gp.maxWorldCol) {
					col = 0;
					row++;
				}
			}
			br.close();

		} catch (Exception e) {
			new Exception(row + 1 + " " + (col + 1), e).printStackTrace();
		}

	}

	public void update() {
		int worldCol = 0;
		int worldRow = 0;

		if (group == null) {
			group = new Group();
			getChildren().add(group);
		}

		while (worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow) {
			int tileNum = mapTileNum[worldCol][worldRow];

			int worldX = worldCol * gp.Bg;
			int worldY = worldRow * gp.Bg;
			int screenX = worldX - gp.player.worldX + gp.player.screenX;
			int screenY = worldY - gp.player.worldY + gp.player.screenY;

			if (worldX + gp.Bg > gp.player.worldX - gp.player.screenX
				&& worldX - gp.Bg < gp.player.worldX + gp.player.screenX
				&& worldY + gp.Bg > gp.player.worldY - gp.player.screenY
				&& worldY - gp.Bg < gp.player.worldY + gp.player.screenY) {
				TextureHolder th = null;
				if (group.getChildren().size() > worldRow * 50 + worldCol) th = (TextureHolder) group.getChildren().get(worldRow * 50 + worldCol);
				if (th == null) {
					th = new TextureHolder(tile.get(tileNum < tile.size() ? tileNum : 0), screenX, screenY);
					group.getChildren().add(worldRow * 50 + worldCol, th);
				} else {
					th.setLayoutX(screenX);
					th.setLayoutY(screenY);
				}
				th.setVisible(true);
			} else {
				TextureHolder th = null;
				if (group.getChildren().size() > worldRow * 50 + worldCol)
					th = (TextureHolder) group.getChildren().get(worldRow * 50 + worldCol);
				if (th != null) th.setVisible(false);
				else {
					th = new TextureHolder(tile.get(tileNum < tile.size() ? tileNum : 0), screenX, screenY);
					th.setVisible(false);
					group.getChildren().add(worldRow * 50 + worldCol, th);
				}
			}

			worldCol++;

			if (worldCol == 50) {
				worldCol = 0;
				worldRow++;
			}
		}
	}

}
