
import { parse } from 'Path'


export function filename ( path ){
    return parse(path).name;
}
