# Jukai-scala
Tokenizer module implemented in scala.

## Tokenizer@JukainLP

### Construction
Tokenizer of [`JukaiNLP`](https://github.com/hshindo/JukaiNLP.jl.git). 

```julia
function Tokenizer()
    dict = IdDict(map(UTF8String, ["UNKNOWN"," ","\n"]))
    T = Float32
    embed = Embedding(T, 100, 10)
    conv = Conv(T, (10,7,), (1,100), paddims=(0,3)
    ls = Linear(T, 100, 10)
    ls2 = Linear(T, 10, 3)
    g = @graph begin
        chars = identity(:chars)
        x = Var(reshape(chars, 1, length(chars)))
        x = embed(x)
        x = conv(x)
        x = reshape(x, size(x, 2), size(x, 3))
        x = transpose(x)
        x = relu(x)
        x = ls(x)
        x = ls2(x)
        x
    end
    Tokenizer(dict, IOE(), g)
end
```

### Requirement
- HDF5 Library.

### Usage
```bash
java -Djava.library.path=/path/to/hdf5library -cp "./target/*" jukaiScala.main.JukaiNLP INPUT_MODEL_FILE"
```

