interface IMovie {
    id: number;
    title: string;
    title_english: string;
    title_long: string;
    summary: string;
    synopsis: string;
    background_image: string;
    background_image_original: string;
    date_uploaded: string;
    date_uploaded_unix: number;
    description_full: string;
    genres: Array<string>
    imdb_code: string;
    language: string;
    large_cover_image: string;
    medium_cover_image: string;
    mpa_rating: string;
    rating: number;
    runtime: number;
    slug: string;
    small_cover_image: string;
    state:string;
    url: string;
    year: number;
    yt_trailer_code: string;
}


interface ICast {
    name: string;
    character_name: string;
    url_small_image: string;
  }

  interface IMovieDetail {
    id: number;
    title: string;
    title_english: string;
    title_long: string;
    cast: Array<ICast>;
    description_full: string;
    description_intro: string;
    genres: Array<string>;
    large_cover_image: string;
    large_screenshot_image1: string;
    large_screenshot_image2: string;
    large_screenshot_image3: string;
    like_count: number;
    rating: number;
    runtime: number;
    year: number;
  }
  