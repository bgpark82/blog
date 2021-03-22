#Quadkey

### 계산법
```$xslt
export const getQuadkeys = (bounds, zoom) => {
    // 타일 크기 (px)
    const TILE_SIZE = 256;

    // 화면 왼쪽 상단 좌표, 오른쪽 하단 좌표, 2^zoom
    const nw = bounds.nw;
    const se = bounds.se;
    const scale = 1 << zoom;

    // mercator 좌표로 변환
    const worldNW = merc.fromLatLngToPoint(nw);
    const worldSE = merc.fromLatLngToPoint(se);

    // pixel 좌표로 변환
    const pixelNW = new window.maps.Point(
        Math.floor(worldNW.x * scale),
        Math.floor(worldNW.y * scale)
    );
    const pixelSE = new window.maps.Point(
        Math.floor(worldSE.x * scale),
        Math.floor(worldSE.y * scale)
    );

    // tile 좌표로 변환
    const tileNW = new window.maps.Point(
        Math.floor(pixelNW.x / TILE_SIZE),
        Math.floor(pixelNW.y / TILE_SIZE)
    );
    const tileSE = new window.maps.Point(
        Math.floor(pixelSE.x / TILE_SIZE),
        Math.floor(pixelSE.y / TILE_SIZE)
    );

    // 화면의 가로, 세로 tile의 개수
    const col = tileSE.x - tileNW.x + 1;
    const row = tileSE.y - tileNW.y + 1;

    let tiles = [];
    for (let i = 0; i < row; i++) {
        for (let j = 0; j < col; j++) {
            tiles.push({
                x: tileNW.x + j,
                y: tileNW.y + i,
            });
        }
    }

    // quadkey 리스트
    return tiles.map((tile) => TileXYToQuadKey(tile.x, tile.y, zoom));
};
```
* 화면의 줌 레벨에서 보여지는 모든 타일들의 quadkey의 리스트를 구한다.
 