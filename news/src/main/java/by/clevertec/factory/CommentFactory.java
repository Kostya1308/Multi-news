package by.clevertec.factory;

import by.clevertec.entity.Comment;
import by.clevertec.entity.Reaction;

public class CommentFactory extends ReactionFactory {
    @Override
    public Reaction create() {
        return new Comment();
    }
}
