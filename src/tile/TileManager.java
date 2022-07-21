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
		loadMap("/res/maps/lavaMap.txt");
	}

	public void getTileImage() {

		try {
			//
			// Arrays.asList(new
			// File(getClass().getResource("res/tiles").toURI()).listFiles()).forEach(f -> {
			//				try {
			//					System.out.println(f);
			//					tile.add(new Tile(new Image(new FileInputStream(f))));
			//				} catch (FileNotFoundException e) {
			//					e.printStackTrace();
			//				}
			//			});

			tile.add(new Tile(getClass().getResourceAsStream("/res/tiles/Stein.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/res/tiles/LavaS3.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/res/tiles/SandOG.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/res/tiles/SGELO.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/res/tiles/SGGSU.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/res/tiles/SGMELO.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/res/tiles/Sand.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/res/tiles/GrasBoden.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/res/tiles/SGEMRU.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/res/tiles/Pflanze.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/res/tiles/SandOGL.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/res/tiles/SGGSR.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/res/tiles/SGGMMERU.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/res/tiles/SGGSL.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/res/tiles/SGGSU.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/res/tiles/SGELU.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/res/tiles/SGGSOW.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/res/tiles/SGGMMERO.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/res/tiles/SandOGU.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/res/tiles/Wasser.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/res/tiles/SteinWandWasser.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/res/tiles/SELU.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/res/tiles/SteinWandWasserGerade.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/res/tiles/SteinWandWasserEcke.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/res/tiles/SteinWandWasserEckeC.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/res/tiles/SteinWandWasserEckeR.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/res/tiles/SteinWandWasserEckeF.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/res/tiles/SteinWandWasserEckeRR.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/res/tiles/GrasEcke.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/res/tiles/SGGSO.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/res/tiles/SteinWandWasserEckeRG.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/res/tiles/SGERU.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/res/tiles/GrasEckeLO.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/res/tiles/GrasSas.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/res/tiles/SteinWandWasserEckeRU.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/res/tiles/SteinWandWasserEckeRRU.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/res/tiles/SERUU.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/res/tiles/SGERO.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/res/tiles/GrasEckeLU.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/res/tiles/SteinWandWasserEckeLU.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/res/tiles/SteinWandWasserEckeLLU.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/res/tiles/GrasEckeFF.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/res/tiles/Hoffnung.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/res/tiles/Hope2.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/res/tiles/SteinWandWasserEckeLG.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/res/tiles/Hope3.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/res/tiles/Hope4.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/res/tiles/Hope5.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/res/tiles/Gras.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/res/tiles/Hope6.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/res/tiles/DGERU.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/res/tiles/AWDAD.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/res/tiles/GEGEGE.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/res/tiles/DGERU2.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/res/tiles/AWDAD2.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/res/tiles/GEGEGE2.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/res/tiles/DGERU3.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/res/tiles/AWDAD3.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/res/tiles/GEGEGE3.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/res/tiles/DGERU4.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/res/tiles/AWDAD4.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/res/tiles/GEGEGE4.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/res/tiles/Nanana.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/res/tiles/Puff.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/res/tiles/FFFF.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/res/tiles/Zaun.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/res/tiles/Zaun2.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/res/tiles/ZaunS.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/res/tiles/ZaunF.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/res/tiles/ZaunFF.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/res/tiles/ZaunSS.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/res/tiles/DM.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/res/tiles/DL.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/res/tiles/Eingang.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/res/tiles/HL.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/res/tiles/DR.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/res/tiles/HR.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/res/tiles/HSL.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/res/tiles/HSRU.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/res/tiles/HSLU.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/res/tiles/HSRO.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/res/tiles/HSLO.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/res/tiles/HSR.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/res/tiles/Baum.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/res/tiles/ALMU.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/res/tiles/ALEU.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/res/tiles/ALSML.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/res/tiles/ALEO.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/res/tiles/AMF.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/res/tiles/ALSMO.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/res/tiles/AREO.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/res/tiles/ALSMR.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/res/tiles/AREU.png"), gp));
			tile.add(new Tile(getClass().getResourceAsStream("/res/tiles/SandOGR.png"), gp));

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
